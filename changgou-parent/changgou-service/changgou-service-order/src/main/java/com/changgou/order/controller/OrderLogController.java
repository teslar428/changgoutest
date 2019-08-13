package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.entity.TokenDecode;
import com.changgou.order.pojo.OrderLog;
import com.changgou.order.service.OrderLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orderLog")
@CrossOrigin
public class OrderLogController {

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private TokenDecode tokenDecode;

    // OrderLog分页条件搜索实现
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) OrderLog orderLog, @PathVariable("page") int page, @PathVariable("size") int size) {
        //调用OrderLogService实现分页条件查询OrderLog
        PageInfo<OrderLog> pageInfo = orderLogService.findPage(orderLog, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // OrderLog分页搜索实现
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        //调用OrderLogService实现分页查询OrderLog
        PageInfo<OrderLog> pageInfo = orderLogService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // 多条件搜索数据
    @PostMapping(value = "/search")
    public Result<List<OrderLog>> findList(@RequestBody(required = false) OrderLog orderLog) {
        //调用OrderLogService实现条件查询OrderLog
        List<OrderLog> list = orderLogService.findList(orderLog);
        return new Result<List<OrderLog>>(true, StatusCode.OK, "查询成功", list);
    }

    // 根据ID删除数据
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") String id) {
        //调用OrderLogService实现根据主键删除
        orderLogService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    // 修改OrderLog数据
    @PutMapping("/{id}")
    public Result update(@RequestBody OrderLog orderLog, @PathVariable("id") String id) {
        //设置主键值
        orderLog.setId(id);
        //调用OrderLogService实现修改OrderLog
        orderLogService.update(orderLog);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    // 新增OrderLog数据
    @PostMapping
    public Result add(@RequestBody OrderLog orderLog) {
        //调用OrderLogService实现添加OrderLog
        orderLogService.add(orderLog);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    // 根据ID查询OrderLog数据
    @GetMapping("/{id}")
    public Result<OrderLog> findById(@PathVariable("id") String id) {
        //调用OrderLogService实现根据主键查询OrderLog
        OrderLog orderLog = orderLogService.findById(id);
        return new Result<OrderLog>(true, StatusCode.OK, "查询成功", orderLog);
    }

    // 查询OrderLog全部数据
    @GetMapping
    public Result<List<OrderLog>> findAll() {
        //调用OrderLogService实现查询所有OrderLog
        List<OrderLog> list = orderLogService.findAll();
        return new Result<List<OrderLog>>(true, StatusCode.OK, "查询成功", list);
    }

    @GetMapping("/query")
    public Result queryOrderLog(String orderId) {
        String username = tokenDecode.getUserInfo().get("username");
        OrderLog orderLog = orderLogService.queryOrderLog(username, orderId);
        return new Result(true, StatusCode.OK, "查询订单日志成功", orderLog);
    }
}
