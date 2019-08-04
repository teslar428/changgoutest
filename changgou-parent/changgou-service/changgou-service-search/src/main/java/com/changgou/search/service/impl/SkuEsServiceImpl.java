package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuEsService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkuEsServiceImpl implements SkuEsService {
    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    //导入Sku数据到es
    @Override
    public void importSku() {
        //调用changgou-service-goods微服务
        Result<List<Sku>> skuListResult = skuFeign.findByStatus("1");
        //将数据转成search.Sku
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(skuListResult.getData()), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec());
            skuInfo.setSpecMap(specMap);
        }
        skuEsMapper.saveAll(skuInfos);
    }

    @Override
    public Map search(Map<String, String> searchMap) {
        //1.条件构建
        NativeSearchQueryBuilder builder = buildBasicQuery(searchMap);

        //2.搜索列表
        Map resultMap = searchList(builder);

        //分组搜索
        List<String> categoryList = searchCategoryList(builder);
        resultMap.put("categoryList",categoryList);

        return resultMap;
    }

    // 构建基本查询
    private NativeSearchQueryBuilder buildBasicQuery(Map<String, String> searchMap) {
        // 查询构建器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        if (searchMap != null) {
            //1.关键字查询
            if (!StringUtils.isEmpty(searchMap.get("keywords"))) {
                nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", searchMap.get("keywords")));
            }
        }
        return nativeSearchQueryBuilder;
    }

    // 数据搜索
    private Map searchList(NativeSearchQueryBuilder builder) {
        Map resultMap = new HashMap();//返回结果
        //查询解析器
        NativeSearchQuery searchQuery = builder.build();
        Page<SkuInfo> skuPage = esTemplate.queryForPage(searchQuery, SkuInfo.class);

        //存储对应数据
        resultMap.put("rows", skuPage.getContent());
        resultMap.put("totalPages", skuPage.getTotalPages());
        return resultMap;
    }

    // 搜索分类分组数据
    public List<String> searchCategoryList(NativeSearchQueryBuilder builder){
        /***
         * 指定分类域，并根据分类域配置聚合查询
         * 1:给分组查询取别名
         * 2:指定分组查询的域
         */
        builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));

        //执行搜索
        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(builder.build(), SkuInfo.class);

        //获取所有分组查询的数据
        Aggregations aggregations = skuPage.getAggregations();
        //从所有数据中获取别名为skuCategory的数据
        StringTerms terms = aggregations.get("skuCategory");

        //分装List集合，将搜索结果存入到List集合中
        List<String> categoryList = new ArrayList<String>();
        for (StringTerms.Bucket bucket : terms.getBuckets()) {
            categoryList.add(bucket.getKeyAsString());
        }
        return categoryList;
    }
}
