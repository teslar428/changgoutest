package com.changgou.item.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    //生成静态页面
    @RequestMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable("id") Long id){
        pageService.createPageHtml(id);
        return new Result(true,StatusCode.OK,"成功生成页面");
    }

    @RequestMapping("/deleteHtml/{id}")
    public Result deleteHtml(@PathVariable("id") Long id){
        pageService.deletePageHtml(id);
        return new Result(true,StatusCode.OK,"成功删除页面");
    }
}
