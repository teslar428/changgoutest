package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.SkuEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuEsController {

    @Autowired
    private SkuEsService skuEsService;

    // 导入数据
    @GetMapping("/import")
    public Result search() {
        skuEsService.importSku();
        return new Result(true, StatusCode.OK, "导入数据到索引库中成功！");
    }

    @PostMapping
    public Map search(@RequestBody(required = false) Map searchMap){
        return  skuEsService.search(searchMap);
    }
}