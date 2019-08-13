package com.changgou.goods.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    // Sku分页条件搜索实现
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Sku sku, @PathVariable("page") int page, @PathVariable("size") int size) {
        //调用SkuService实现分页条件查询Sku
        PageInfo<Sku> pageInfo = skuService.findPage(sku, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // Sku分页搜索实现
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        //调用SkuService实现分页查询Sku
        PageInfo<Sku> pageInfo = skuService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // 多条件搜索数据
    @PostMapping(value = "/search")
    public Result<List<Sku>> findList(@RequestBody(required = false) Sku sku) {
        //调用SkuService实现条件查询Sku
        List<Sku> list = skuService.findList(sku);
        return new Result<List<Sku>>(true, StatusCode.OK, "查询成功", list);
    }

    // 根据ID删除数据
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        //调用SkuService实现根据主键删除
        skuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    // 修改Sku数据
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Sku sku, @PathVariable("id") Long id) {
        //设置主键值
        sku.setId(id);
        //调用SkuService实现修改Sku
        skuService.update(sku);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    // 新增Sku数据
    @PostMapping
    public Result add(@RequestBody Sku sku) {
        //调用SkuService实现添加Sku
        skuService.add(sku);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    // 根据ID查询Sku数据
    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable("id") Long id) {
        //调用SkuService实现根据主键查询Sku
        Sku sku = skuService.findById(id);
        return new Result<Sku>(true, StatusCode.OK, "查询成功", sku);
    }

    // 查询Sku全部数据
    @GetMapping
    public Result<List<Sku>> findAll() {
        //调用SkuService实现查询所有Sku
        List<Sku> list = skuService.findAll();
        return new Result<List<Sku>>(true, StatusCode.OK, "查询成功", list);
    }

    // 根据审核状态查询Sku
    @GetMapping("/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable("status") String status) {
        List<Sku> skuList = skuService.findByStatus(status);
        return new Result<List<Sku>>(true, StatusCode.OK, "查询成功", skuList);
    }

    //库存递减
    @PostMapping("/decr/count")
    public Result decrCount(String username) {
        skuService.decrCount(username);
        return new Result(true, StatusCode.OK, "库存递减成功");
    }

    @PostMapping("/incr/count")
    public Result incrCount(String username) {
        skuService.incrCount(username);
        return new Result(true, StatusCode.OK, "库存回滚成功");
    }
}
