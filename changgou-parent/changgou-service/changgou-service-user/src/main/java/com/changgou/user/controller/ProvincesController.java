package com.changgou.user.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.user.pojo.Provinces;
import com.changgou.user.service.ProvincesService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/provinces")
@CrossOrigin
public class ProvincesController {

    @Autowired
    private ProvincesService provincesService;

    // Provinces分页条件搜索实现
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Provinces provinces, @PathVariable("page")  int page, @PathVariable("size")  int size){
        //调用ProvincesService实现分页条件查询Provinces
        PageInfo<Provinces> pageInfo = provincesService.findPage(provinces, page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    // Provinces分页搜索实现
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable("page")  int page, @PathVariable("size")  int size){
        //调用ProvincesService实现分页查询Provinces
        PageInfo<Provinces> pageInfo = provincesService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    // 多条件搜索数据
    @PostMapping(value = "/search" )
    public Result<List<Provinces>> findList(@RequestBody(required = false)  Provinces provinces){
        //调用ProvincesService实现条件查询Provinces
        List<Provinces> list = provincesService.findList(provinces);
        return new Result<List<Provinces>>(true,StatusCode.OK,"查询成功",list);
    }

    // 根据ID删除数据
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable("id") String id){
        //调用ProvincesService实现根据主键删除
        provincesService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    // 修改Provinces数据
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Provinces provinces,@PathVariable("id") String id){
        //设置主键值
        provinces.setProvinceid(id);
        //调用ProvincesService实现修改Provinces
        provincesService.update(provinces);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    // 新增Provinces数据
    @PostMapping
    public Result add(@RequestBody   Provinces provinces){
        //调用ProvincesService实现添加Provinces
        provincesService.add(provinces);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    // 根据ID查询Provinces数据
    @GetMapping("/{id}")
    public Result<Provinces> findById(@PathVariable("id") String id){
        //调用ProvincesService实现根据主键查询Provinces
        Provinces provinces = provincesService.findById(id);
        return new Result<Provinces>(true,StatusCode.OK,"查询成功",provinces);
    }

    // 查询Provinces全部数据
    @GetMapping
    public Result<List<Provinces>> findAll(){
        //调用ProvincesService实现查询所有Provinces
        List<Provinces> list = provincesService.findAll();
        return new Result<List<Provinces>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
