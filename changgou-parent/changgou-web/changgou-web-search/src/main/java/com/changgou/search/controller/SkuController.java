package com.changgou.search.controller;

import com.changgou.entity.Page;
import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/search")
public class SkuController {
    @Autowired
    private SkuFeign skuFeign;

    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map searchMap, Model model) {
        //转换特殊字符
        handleSearchMap(searchMap);
        //调用changgou-service-search微服务
        Map<String, Object> resultMap = skuFeign.search(searchMap);
        //搜索数据结果
        model.addAttribute("result", resultMap);

        //分页计算
        Page<SkuInfo> page = new Page<SkuInfo>(
                Long.parseLong(resultMap.get("totalPages").toString()),
                Integer.parseInt(resultMap.get("pageNum").toString()),
                Integer.parseInt(resultMap.get("pageSize").toString())
        );
        model.addAttribute("page", page);

        //搜索条件
        model.addAttribute("searchMap", searchMap);

        //请求地址
        String[] urls = url(searchMap);
        model.addAttribute("url", urls[0]);
        model.addAttribute("sorturl", urls[1]);
        return "search";
    }

    private String[] url(Map<String, String> searchMap) {
        String url = "/search/list";
        String sorturl = "/search/list";
        if (searchMap != null && searchMap.size() > 0) {
            url += "?";
            sorturl += "?";
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                url += key + "=" + entry.getValue() + "&";
                if (key.equalsIgnoreCase("sortRule") || key.equalsIgnoreCase("sortField") || key.equalsIgnoreCase("pageNum")) {
                    continue;
                }
                sorturl += key + "=" + entry.getValue() + "&";
            }
            //去掉最后一个&
            url = url.substring(0, url.length() - 1);
            sorturl = sorturl.substring(0, sorturl.length() - 1);
        }
        return new String[]{url, sorturl};
    }

    //替换特殊字符
    private void handleSearchMap(Map<String, String> searchMap) {
        if (searchMap != null) {
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                if (entry.getKey().startsWith("spec_")) {
                    entry.setValue(entry.getValue().replace("+", "%2B"));
                }
            }
        }
    }
}
