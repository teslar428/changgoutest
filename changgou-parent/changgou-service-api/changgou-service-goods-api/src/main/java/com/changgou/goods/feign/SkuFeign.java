package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    // Sku分页条件搜索实现
    @PostMapping("/search/{page}/{size}")
    Result<PageInfo> findPage(@RequestBody(required = false) Sku sku, @PathVariable("page") int page, @PathVariable("size") int size);

    // Sku分页搜索实现
    @GetMapping("/search/{page}/{size}")
    Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size);

    // 多条件搜索品牌数据
    @PostMapping("/search")
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

    // 根据ID删除品牌数据
    @DeleteMapping("/{id}")
    Result delete(@PathVariable("id") Long id);

    // 修改Sku数据
    @PutMapping("/{id}")
    Result update(@RequestBody Sku sku, @PathVariable("id") Long id);

    // 新增Sku数据
    @PostMapping
    Result add(@RequestBody Sku sku);

    // 根据ID查询Sku数据
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable("id") Long id);

    // 查询Sku全部数据
    @GetMapping
    Result<List<Sku>> findAll();

    //根据审核状态查询Sku
    @GetMapping("/status/{status}")
    Result findByStatus(@PathVariable("status") String status);

}