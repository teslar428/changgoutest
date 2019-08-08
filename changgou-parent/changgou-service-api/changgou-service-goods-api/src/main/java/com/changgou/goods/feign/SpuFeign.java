package com.changgou.goods.feign;
import com.changgou.entity.Result;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="goods")
@RequestMapping("/spu")
public interface SpuFeign {

    // Spu分页条件搜索实现
    @PostMapping("/search/{page}/{size}")
    Result<PageInfo> findPage(@RequestBody(required = false) Spu spu, @PathVariable("page") int page, @PathVariable("size") int size);

    // Spu分页搜索实现
    @GetMapping("/search/{page}/{size}")
    Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size);

    // 多条件搜索品牌数据
    @PostMapping("/search")
    Result<List<Spu>> findList(@RequestBody(required = false) Spu spu);

    // 根据ID删除品牌数据
    @DeleteMapping("/{id}")
    Result delete(@PathVariable("id") Long id);

    // 修改Spu数据
    @PutMapping("/{id}")
    Result update(@RequestBody Spu spu, @PathVariable("id") Long id);

    // 新增Spu数据
    @PostMapping
    Result add(@RequestBody Spu spu);

    // 根据ID查询Spu数据
    @GetMapping("/{id}")
    Result<Spu> findById(@PathVariable("id") Long id);

    // 查询Spu全部数据
    @GetMapping
    Result<List<Spu>> findAll();
}