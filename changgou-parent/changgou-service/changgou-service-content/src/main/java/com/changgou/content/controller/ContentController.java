package com.changgou.content.controller;

import com.changgou.content.pojo.Content;
import com.changgou.content.service.ContentService;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content")
@CrossOrigin
public class ContentController {

    @Autowired
    private ContentService contentService;

    // Content分页条件搜索实现
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Content content, @PathVariable("page") int page, @PathVariable("size") int size) {
        //调用ContentService实现分页条件查询Content
        PageInfo<Content> pageInfo = contentService.findPage(content, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // Content分页搜索实现
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        //调用ContentService实现分页查询Content
        PageInfo<Content> pageInfo = contentService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // 多条件搜索数据
    @PostMapping(value = "/search")
    public Result<List<Content>> findList(@RequestBody(required = false) Content content) {
        //调用ContentService实现条件查询Content
        List<Content> list = contentService.findList(content);
        return new Result<List<Content>>(true, StatusCode.OK, "查询成功", list);
    }

    // 根据ID删除数据
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        //调用ContentService实现根据主键删除
        contentService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    // 修改Content数据
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Content content, @PathVariable("id") Long id) {
        //设置主键值
        content.setId(id);
        //调用ContentService实现修改Content
        contentService.update(content);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    // 新增Content数据
    @PostMapping
    public Result add(@RequestBody Content content) {
        //调用ContentService实现添加Content
        contentService.add(content);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    // 根据ID查询Content数据
    @GetMapping("/{id}")
    public Result<Content> findById(@PathVariable("id") Long id) {
        //调用ContentService实现根据主键查询Content
        Content content = contentService.findById(id);
        return new Result<Content>(true, StatusCode.OK, "查询成功", content);
    }

    // 查询Content全部数据
    @GetMapping
    public Result<List<Content>> findAll() {
        //调用ContentService实现查询所有Content
        List<Content> list = contentService.findAll();
        return new Result<List<Content>>(true, StatusCode.OK, "查询成功", list);
    }

    @GetMapping("/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable("id") Long id) {
        List<Content> contentList = contentService.findByCategory(id);
        return new Result<List<Content>>(true, StatusCode.OK, "查询成功", contentList);
    }
}
