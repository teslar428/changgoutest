package com.changgou.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.changgou.entity.Result;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@CanalEventListener
public class CanalDataEventListener {
    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 增加数据监听
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList().forEach((c) -> System.out.println("增加数据监听: " + c.getName() + " ::   " + c.getValue()));
    }

    // 修改数据监听
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList().forEach((c) -> System.out.println("修改数据监听: " + c.getName() + " ::   " + c.getValue()));
    }

    // 删除数据监听
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType) {
        System.out.println("删除数据监听......");
    }

    // 获取指定列的值
    public String getColumn(CanalEntry.RowData rowData, String columnName) {
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            if (column.getName().equals(columnName)) {
                return column.getValue();
            }
        }

        //有可能是删除操作
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            if (column.getName().equals(columnName)) {
                return column.getValue();
            }
        }
        return null;
    }

    // 广告数据修改监听
    @ListenPoint(destination = "example", schema = "changgou_content", table = {"tb_content_category", "tb_content"}, eventType = CanalEntry.EventType.UPDATE)
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList().forEach((c) -> System.out.println("自定义数据修改监听: " + c.getName() + " ::   " + c.getValue()));

        //获取广告分类的ID
        String categoryId = getColumn(rowData, "category_id");
        //根据广告分类ID获取所有广告
        Result<List<Content>> result = contentFeign.findByCategory(Long.valueOf(categoryId));
        //将广告数据存入到Redis缓存
        List<Content> contents = result.getData();
        stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(contents));
    }
}