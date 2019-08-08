package com.changgou.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.canal.mq.queue.TopicQueue;
import com.changgou.canal.mq.send.TopicMessageSender;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.changgou.entity.Message;
import com.changgou.entity.Result;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@CanalEventListener
public class CanalDataEventListener {
    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ngxLinkOri}")
    private String ngxLinkOri;

    @Autowired
    private TopicMessageSender topicMessageSender;

    // 增加数据监听
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            System.out.println("增加数据监听: " + column.getName() + " ::   " + column.getValue());
//        }
    }

    // 修改数据监听
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            System.out.println("修改数据监听: " + column.getName() + " ::   " + column.getValue());
//        }
    }

    // 删除数据监听
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
//            System.out.println("删除数据监听:" + column.getName() + " ::   " + column.getValue());
//        }
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
    @ListenPoint(destination = "example", schema = "changgou_content", table = {"tb_content"}, eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //获取广告分类的ID
        String categoryId = getColumn(rowData, "category_id");
        //根据广告分类ID获取所有广告
        Result<List<Content>> result = contentFeign.findByCategory(Long.valueOf(categoryId));
        //将广告数据存入到Redis缓存

        List<Content> contents = result.getData();
        if (contents != null) {
            stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(contents));
        }
        String ngxLink = ngxLinkOri;
        ngxLink += ("id=" + categoryId);

        restTemplate.getForObject(ngxLink, String.class);
    }

    @ListenPoint(destination = "example", schema = "changgou_goods", table = {"tb_spu"}, eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.DELETE})
    public void onEventCustomSpu(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //操作类型
        int number = eventType.getNumber();
        //操作的数据
        String id = getColumn(rowData, "id");
        System.out.println("ID:"+id);
        //封装Message
        Message message = new Message(number, id, TopicQueue.TOPIC_QUEUE_SPU, TopicQueue.TOPIC_EXCHANGE_SPU);
        topicMessageSender.sendMessage(message);
    }
}