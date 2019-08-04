package com.changgou.goods.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController {

    @Autowired
    private SpuService spuService;

    // Spu分页条件搜索实现
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Spu spu, @PathVariable("page") int page, @PathVariable("size") int size) {
        //调用SpuService实现分页条件查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // Spu分页搜索实现
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        //调用SpuService实现分页查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // 多条件搜索数据
    @PostMapping(value = "/search")
    public Result<List<Spu>> findList(@RequestBody(required = false) Spu spu) {
        //调用SpuService实现条件查询Spu
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true, StatusCode.OK, "查询成功", list);
    }

    // 根据ID删除数据
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        //调用SpuService实现根据主键删除
        spuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    // 修改Spu数据
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Spu spu, @PathVariable("id") Long id) {
        //设置主键值
        spu.setId(id);
        //调用SpuService实现修改Spu
        spuService.update(spu);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    // 新增Spu数据
    @PostMapping
    public Result add(@RequestBody Spu spu) {
        //调用SpuService实现添加Spu
        spuService.add(spu);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    // 根据ID查询Spu数据
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable("id") Long id) {
        //调用SpuService实现根据主键查询Spu
        Spu spu = spuService.findById(id);
        return new Result<Spu>(true, StatusCode.OK, "查询成功", spu);
    }

    // 查询Spu全部数据
    @GetMapping
    public Result<List<Spu>> findAll() {
        //调用SpuService实现查询所有Spu
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK, "查询成功", list);
    }

    @PostMapping("/goods")
    public Result goods(@RequestBody Goods goods) {
        spuService.saveGoods(goods);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @GetMapping("/goods/{id}")
    public Result<Goods> findGoodsById(@PathVariable("id") Long id) {
        Goods goods = spuService.findGoodsById(id);
        return new Result<Goods>(true, StatusCode.OK, "查询成功", goods);
    }

    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable("id") Long id) {
        spuService.audit(id);
        return new Result(true, StatusCode.OK, "审核成功");
    }

    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable("id") Long id) {
        spuService.pull(id);
        return new Result(true, StatusCode.OK, "下架成功");
    }

    @PutMapping("/put/{id}")
    public Result put(@PathVariable("id") Long id) {
        spuService.put(id);
        return new Result(true, StatusCode.OK, "上架成功");
    }

    @PutMapping("/put/many")
    public Result putMany(@RequestBody Long[] ids) {
        int count = spuService.putMany(ids);
        return new Result(true, StatusCode.OK, "成功上架" + count + "个商品");
    }

    @PutMapping("/pull/many")
    public Result pullMany(@RequestBody Long[] ids) {
        int count = spuService.pullMany(ids);
        return new Result(true, StatusCode.OK, "成功下架" + count + "个商品");
    }

    @DeleteMapping("/logic/delete/{id}")
    public Result logicDelete(@PathVariable("id") Long id) {
        spuService.logicDelete(id);
        return new Result(true, StatusCode.OK, "商品成功放入回收站");
    }

    @PutMapping("/restore/{id}")
    public Result restore(@PathVariable("id") Long id){
        spuService.restore(id);
        return new Result(true,StatusCode.OK,"商品成功从回收站还原");
    }
}
