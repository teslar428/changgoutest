package com.changgou.seckill.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.entity.TokenDecode;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/seckillOrder")
@CrossOrigin
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private TokenDecode tokenDecode;

    // SeckillOrder分页条件搜索实现
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) SeckillOrder seckillOrder, @PathVariable("page") int page, @PathVariable("size") int size) {
        //调用SeckillOrderService实现分页条件查询SeckillOrder
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(seckillOrder, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // SeckillOrder分页搜索实现
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        //调用SeckillOrderService实现分页查询SeckillOrder
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // 多条件搜索数据
    @PostMapping(value = "/search")
    public Result<List<SeckillOrder>> findList(@RequestBody(required = false) SeckillOrder seckillOrder) {
        //调用SeckillOrderService实现条件查询SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findList(seckillOrder);
        return new Result<List<SeckillOrder>>(true, StatusCode.OK, "查询成功", list);
    }

    // 根据ID删除数据
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        //调用SeckillOrderService实现根据主键删除
        seckillOrderService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    // 修改SeckillOrder数据
    @PutMapping("/{id}")
    public Result update(@RequestBody SeckillOrder seckillOrder, @PathVariable("id") Long id) {
        //设置主键值
        seckillOrder.setId(id);
        //调用SeckillOrderService实现修改SeckillOrder
        seckillOrderService.update(seckillOrder);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    // 新增SeckillOrder数据
//    @PostMapping
//    public Result add(@RequestBody SeckillOrder seckillOrder) {
//        //调用SeckillOrderService实现添加SeckillOrder
//        seckillOrderService.add(seckillOrder);
//        return new Result(true, StatusCode.OK, "添加成功");
//    }

    // 根据ID查询SeckillOrder数据
    @GetMapping("/{id}")
    public Result<SeckillOrder> findById(@PathVariable("id") Long id) {
        //调用SeckillOrderService实现根据主键查询SeckillOrder
        SeckillOrder seckillOrder = seckillOrderService.findById(id);
        return new Result<SeckillOrder>(true, StatusCode.OK, "查询成功", seckillOrder);
    }

    // 查询SeckillOrder全部数据
    @GetMapping
    public Result<List<SeckillOrder>> findAll() {
        //调用SeckillOrderService实现查询所有SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findAll();
        return new Result<List<SeckillOrder>>(true, StatusCode.OK, "查询成功", list);
    }

    @RequestMapping("/add")
    public Result add(String time, Long id) {
        try {
            String username = tokenDecode.getUserInfo().get("username");
            Boolean bo = seckillOrderService.add(id, time, username);
            if (bo) {
                return new Result(true, StatusCode.OK, "抢单成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, StatusCode.ERROR, "服务器繁忙，请稍后再试");
    }

    @RequestMapping("/query")
    public Result queryStatus() {
        String username = tokenDecode.getUserInfo().get("username");
        SeckillStatus seckillStatus = seckillOrderService.queryStatus(username);
        if (seckillStatus != null) {
            return new Result(true, seckillStatus.getStatus(), "查询抢购信息成功", seckillStatus);
        } else {
            return new Result(false, StatusCode.NOTFOUNDERROR, "没有抢购信息");
        }
    }
}
