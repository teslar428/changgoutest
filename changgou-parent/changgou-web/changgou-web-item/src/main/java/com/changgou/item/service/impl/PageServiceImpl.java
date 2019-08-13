package com.changgou.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private TemplateEngine templateEngine;

    private Map<String, Object> buildDataModel(Long spuId) {
        Result<Spu> result = spuFeign.findById(spuId);
        Spu spu = result.getData();

        //获取分类信息
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("category1", categoryFeign.findById(spu.getCategory1Id()).getData());
        dataMap.put("category2", categoryFeign.findById(spu.getCategory2Id()).getData());
        dataMap.put("category3", categoryFeign.findById(spu.getCategory3Id()).getData());
        if (spu.getImages() != null) {
            dataMap.put("imageList", spu.getImages().split(","));
        }
        //所有可选规格
        dataMap.put("specificationList", JSON.parseObject(spu.getSpecItems(), Map.class));
        dataMap.put("spu", spu);

        //根据spuId查询sku集合
        Sku skuCondition = new Sku();
        skuCondition.setSpuId(spuId);
        Result<List<Sku>> resultSku = skuFeign.findList(skuCondition);
        dataMap.put("skuList", resultSku.getData());
        return dataMap;
    }

    //生成静态页
    @Override
    public void createPageHtml(Long spuId) {
        Context context = new Context();
        Map<String, Object> dataModel = buildDataModel(spuId);
        context.setVariables(dataModel);

        String path = PageServiceImpl.class.getResource("/").getPath() + "/items";

        //准备文件
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //3.生成页面
        try {
            FileWriter fileWriter = new FileWriter(path + "/" + spuId + ".html");
            templateEngine.process("item", context, fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePageHtml(Long spuId) {
        new File(PageServiceImpl.class.getResource("/").getPath() + "/items", spuId + ".html").delete();
    }

}
