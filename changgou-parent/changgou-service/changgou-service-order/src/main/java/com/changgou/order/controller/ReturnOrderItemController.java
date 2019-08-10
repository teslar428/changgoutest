package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.pojo.ReturnOrderItem;
import com.changgou.order.service.ReturnOrderItemService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/returnOrderItem")
@CrossOrigin
public class ReturnOrderItemController {

    @Autowired
    private ReturnOrderItemService returnOrderItemService;

    // ReturnOrderItem分页条件搜索实现
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) ReturnOrderItem returnOrderItem, @PathVariable("page") int page, @PathVariable("size") int size) {
        //调用ReturnOrderItemService实现分页条件查询ReturnOrderItem
        PageInfo<ReturnOrderItem> pageInfo = returnOrderItemService.findPage(returnOrderItem, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // ReturnOrderItem分页搜索实现
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        //调用ReturnOrderItemService实现分页查询ReturnOrderItem
        PageInfo<ReturnOrderItem> pageInfo = returnOrderItemService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // 多条件搜索数据
    @PostMapping(value = "/search")
    public Result<List<ReturnOrderItem>> findList(@RequestBody(required = false) ReturnOrderItem returnOrderItem) {
        //调用ReturnOrderItemService实现条件查询ReturnOrderItem
        List<ReturnOrderItem> list = returnOrderItemService.findList(returnOrderItem);
        return new Result<List<ReturnOrderItem>>(true, StatusCode.OK, "查询成功", list);
    }

    // 根据ID删除数据
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        //调用ReturnOrderItemService实现根据主键删除
        returnOrderItemService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    // 修改ReturnOrderItem数据
    @PutMapping("/{id}")
    public Result update(@RequestBody ReturnOrderItem returnOrderItem, @PathVariable("id") Long id) {
        //设置主键值
        returnOrderItem.setId(id);
        //调用ReturnOrderItemService实现修改ReturnOrderItem
        returnOrderItemService.update(returnOrderItem);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    // 新增ReturnOrderItem数据
    @PostMapping
    public Result add(@RequestBody ReturnOrderItem returnOrderItem) {
        //调用ReturnOrderItemService实现添加ReturnOrderItem
        returnOrderItemService.add(returnOrderItem);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    // 根据ID查询ReturnOrderItem数据
    @GetMapping("/{id}")
    public Result<ReturnOrderItem> findById(@PathVariable("id") Long id) {
        //调用ReturnOrderItemService实现根据主键查询ReturnOrderItem
        ReturnOrderItem returnOrderItem = returnOrderItemService.findById(id);
        return new Result<ReturnOrderItem>(true, StatusCode.OK, "查询成功", returnOrderItem);
    }

    // 查询ReturnOrderItem全部数据
    @GetMapping
    public Result<List<ReturnOrderItem>> findAll() {
        //调用ReturnOrderItemService实现查询所有ReturnOrderItem
        List<ReturnOrderItem> list = returnOrderItemService.findAll();
        return new Result<List<ReturnOrderItem>>(true, StatusCode.OK, "查询成功", list);
    }
}
