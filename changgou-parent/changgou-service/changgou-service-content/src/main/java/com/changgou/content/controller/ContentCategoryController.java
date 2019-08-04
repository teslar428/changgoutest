package com.changgou.content.controller;

import com.changgou.content.pojo.ContentCategory;
import com.changgou.content.service.ContentCategoryService;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/contentCategory")
@CrossOrigin
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    // ContentCategory分页条件搜索实现
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  ContentCategory contentCategory, @PathVariable("page")  int page, @PathVariable("size")  int size){
        //调用ContentCategoryService实现分页条件查询ContentCategory
        PageInfo<ContentCategory> pageInfo = contentCategoryService.findPage(contentCategory, page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    // ContentCategory分页搜索实现
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable("page")  int page, @PathVariable("size")  int size){
        //调用ContentCategoryService实现分页查询ContentCategory
        PageInfo<ContentCategory> pageInfo = contentCategoryService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    // 多条件搜索数据
    @PostMapping(value = "/search" )
    public Result<List<ContentCategory>> findList(@RequestBody(required = false)  ContentCategory contentCategory){
        //调用ContentCategoryService实现条件查询ContentCategory
        List<ContentCategory> list = contentCategoryService.findList(contentCategory);
        return new Result<List<ContentCategory>>(true,StatusCode.OK,"查询成功",list);
    }

    // 根据ID删除数据
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用ContentCategoryService实现根据主键删除
        contentCategoryService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    // 修改ContentCategory数据
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  ContentCategory contentCategory,@PathVariable Long id){
        //设置主键值
        contentCategory.setId(id);
        //调用ContentCategoryService实现修改ContentCategory
        contentCategoryService.update(contentCategory);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    // 新增ContentCategory数据
    @PostMapping
    public Result add(@RequestBody   ContentCategory contentCategory){
        //调用ContentCategoryService实现添加ContentCategory
        contentCategoryService.add(contentCategory);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    // 根据ID查询ContentCategory数据
    @GetMapping("/{id}")
    public Result<ContentCategory> findById(@PathVariable Long id){
        //调用ContentCategoryService实现根据主键查询ContentCategory
        ContentCategory contentCategory = contentCategoryService.findById(id);
        return new Result<ContentCategory>(true,StatusCode.OK,"查询成功",contentCategory);
    }

    // 查询ContentCategory全部数据
    @GetMapping
    public Result<List<ContentCategory>> findAll(){
        //调用ContentCategoryService实现查询所有ContentCategory
        List<ContentCategory> list = contentCategoryService.findAll();
        return new Result<List<ContentCategory>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
