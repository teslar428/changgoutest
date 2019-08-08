package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuEsService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        //将List<Sku>转成List<SkuInfo>
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(skuListResult.getData()), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
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

        //用户没有输入分类或者品牌条件的时候,就根据品牌和分类进行分组查询
        //用户如果输入了分类或者品牌,就不用根据分类或者品牌查询数据了,因为页面不需要显示

        //3.分类搜索
        if (searchMap == null || searchMap.get("category") == null) {
            List<String> categoryList = searchCategoryList(builder);
            resultMap.put("categoryList", categoryList);
        }

        //4.查询类型对应的品牌
        if (searchMap == null || searchMap.get("brand") == null) {
            List<String> brandList = searchBrandList(builder);
            resultMap.put("brandList", brandList);
        }

        //5.查询规格数据
        Map<String, Set<String>> specMap = searchSpec(builder);
        resultMap.put("specList", specMap);

        //分页数据保存
        resultMap.put("pageNum", builder.build().getPageable().getPageNumber() + 1);
        resultMap.put("pageSize", builder.build().getPageable().getPageSize());
        return resultMap;
    }

    //构建基本查询
    private NativeSearchQueryBuilder buildBasicQuery(Map<String, String> searchMap) {
        //构建条件
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //构建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        //条件组装
        if (searchMap != null) {
            //关键词
            if (!StringUtils.isEmpty(searchMap.get("keywords"))) {
                queryBuilder.must(QueryBuilders.matchQuery("name", searchMap.get("keywords")));
            }

            //分类筛选
            if (!StringUtils.isEmpty(searchMap.get("category"))) {
                queryBuilder.must(QueryBuilders.matchQuery("categoryName", searchMap.get("category")));
            }

            //品牌
            if (!StringUtils.isEmpty(searchMap.get("brand"))) {
                queryBuilder.must(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
            }

            //规格
            for (String key : searchMap.keySet()) {
                //如果是规格参数
                if (key.startsWith("spec_")) {
                    String value = searchMap.get(key).replace("\\", "");
                    queryBuilder.must(QueryBuilders.matchQuery("specMap." + key.substring(5) + ".keyword", value));
                }
            }

            //价格区间过滤
            //传过来的价格格式:price="500-1000"
            String price = searchMap.get("price");
            if (!StringUtils.isEmpty(price)) {
                //去掉元和以上
                price = price.replace("元", "").replace("以上", "");
                //根据-分割
                String[] array = price.split("-");
                //x<price
                queryBuilder.must(QueryBuilders.rangeQuery("price").gt(array[0]));
                if (array.length == 2) {
                    //price<=y
                    queryBuilder.must(QueryBuilders.rangeQuery("price").lte(array[1]));
                }
            }

            //排序
            String sortRule = searchMap.get("sortRule");//排序规则: ASC DESC
            String sortField = searchMap.get("sortField");//排序字段: price
            if (!StringUtils.isEmpty(sortField)) {
                nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
            }
        }

        //分页
        Integer pageNo = pageConvert(searchMap);//获取页码
        Integer pageSize = sizeConvert(searchMap);//每页显示数量
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        nativeSearchQueryBuilder.withPageable(pageRequest);

        //添加过滤
        nativeSearchQueryBuilder.withQuery(queryBuilder);
        return nativeSearchQueryBuilder;
    }

    //获取当前页
    //  如果不发生异常,则直接转换成数字
    //  如果发生异常,则默认从第一页查询
    private Integer pageConvert(Map<String, String> searchMap) {
        Integer defaultPage = 1;
        try {
            return Integer.parseInt(searchMap.get("pageNum"));
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("未传入当前页,使用默认值......" + defaultPage);
        }
        return defaultPage;
    }

    //获取每页显示数量
    //  如果不发生异常,则直接转换成数字
    //  如果发生异常,则使用默认数据
    private Integer sizeConvert(Map<String, String> searchMap) {
        Integer defaultSize = 10;
        try {
            return Integer.parseInt(searchMap.get("pageSize"));
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("未传入每页显示数量,使用默认值......" + defaultSize);
        }
        return defaultSize;
    }

    //数据搜索
    private Map searchList(NativeSearchQueryBuilder builder) {
        Map resultMap = new HashMap();//返回结果
        //高亮域配置
        HighlightBuilder.Field field = new HighlightBuilder.
                Field("name").                      //指定的高亮域
                preTags("<span style=\"color:red\">").   //前缀
                postTags("</span>").                      //后缀
                fragmentSize(100);

        //添加高亮域
        builder.withHighlightFields(field);
        //查询解析器
        NativeSearchQuery searchQuery = builder.build();
        //分页搜索
        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(searchQuery, SkuInfo.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                //定义一个集合存储所有高亮数据
                List<T> list = new ArrayList<T>();

                //循环所有数据
                for (SearchHit hit : response.getHits()) {
                    //获取非高亮数据           例如：小白真美丽      {"name":"张三","age":27}
                    SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);

                    //获取高亮数据            例如：小白真 <span style="color:red;">美丽</span>
                    HighlightField titleHighlight = hit.getHighlightFields().get("name");      //获取标题的高亮数据

                    if (titleHighlight != null) {
                        //定义一个字符接收高亮数据
                        StringBuffer buffer = new StringBuffer();
                        //循环获取高亮数据
                        for (Text text : titleHighlight.getFragments()) {
                            //text.toString():   小白真<span style="color:red;">美丽</span>啊
                            buffer.append(text.toString());
                        }
                        //将非高亮数据替换成高亮数据    小白真美丽-->小白真 <span style="color:red;">美丽</span>
                        skuInfo.setName(buffer.toString());
                    }

                    //将高亮数据返回
                    list.add((T) skuInfo);
                }
                //1：返回的集合数据   2：分页数据   3：总记录数
                return new AggregatedPageImpl<T>(list, pageable, response.getHits().getTotalHits());
            }
        });

        resultMap.put("rows", skuPage.getContent());
        resultMap.put("totalPages", skuPage.getTotalPages());
        return resultMap;
    }

    //搜索分类分组数据
    private List<String> searchCategoryList(NativeSearchQueryBuilder builder) {
        /***
         * 指定分类域，并根据分类域配置聚合查询
         * 1:给分组查询取别名
         * 2:指定分组查询的域
         */
        String terms = "skuCategory";
        String field = "categoryName";
        builder.addAggregation(AggregationBuilders.terms(terms).field(field));
        //执行搜索
        AggregatedPage<SkuInfo> result = esTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取所有分组查询的数据
        Aggregations aggregations = result.getAggregations();
        //从所有数据中获取别名为skuCategory的数据
        StringTerms stringTerms = aggregations.get(terms);
        //分装List集合，将搜索结果存入到List集合中
        List<String> categoryList = new ArrayList<String>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            categoryList.add(bucket.getKeyAsString());
        }
        return categoryList;
    }

    //查询品牌列表
    private List<String> searchBrandList(NativeSearchQueryBuilder builder) {
        //查询聚合品牌 skuBrandGroupby给聚合分组结果起个别名
        String terms = "skuBrand";
        String field = "brandName";
        builder.addAggregation(AggregationBuilders.terms(terms).field(field));
        //执行搜索
        AggregatedPage<SkuInfo> result = esTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取聚合品牌结果
        Aggregations aggregations = result.getAggregations();
        //获取分组结果
        StringTerms stringTerms = aggregations.get(terms);
        //返回品牌名称
        List<String> sku_brandList = stringTerms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList());
        return sku_brandList;
    }

    //查询规格列表
    private Map<String, Set<String>> searchSpec(NativeSearchQueryBuilder builder) {
        String terms = "skuSpec";
        String field = "spec.keyword";
        builder.addAggregation(AggregationBuilders.terms(terms).field(field));
        AggregatedPage<SkuInfo> result = esTemplate.queryForPage(builder.build(), SkuInfo.class);
        Aggregations aggregations = result.getAggregations();
        StringTerms stringTerms = aggregations.get(terms);

        //返回规格数据名称
        List<String> sku_specList = stringTerms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList());

        //将规格转成Map
        Map<String, Set<String>> specMap = specPutAll(sku_specList);
        return specMap;
    }

    //将所有规格数据转入到Map中
    private Map<String, Set<String>> specPutAll(List<String> specList) {
        //新建一个Map
        Map<String, Set<String>> specMap = new HashMap<String, Set<String>>();

        //将集合数据存入到Map中
        for (String specString : specList) {
            //将Map数据转成Map
            Map<String, String> map = JSON.parseObject(specString, Map.class);

            //循环转换后的Map
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();        //规格名字
                String value = entry.getValue();    //规格选项值
                //获取当前规格名字对应的规格数据
                Set<String> specValues = specMap.get(key);
                if (specValues == null) {
                    specValues = new HashSet<String>();
                }
                //将当前规格加入到集合中
                specValues.add(value);
                //将数据存入到specMap中
                specMap.put(key, specValues);
            }
        }
        return specMap;
    }

    @Override
    public void deleteAll() {
        skuEsMapper.deleteAll();
    }
}
