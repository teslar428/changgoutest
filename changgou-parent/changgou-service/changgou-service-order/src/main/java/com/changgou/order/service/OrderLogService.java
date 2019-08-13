package com.changgou.order.service;

import com.changgou.order.pojo.OrderLog;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderLogService {

    PageInfo<OrderLog> findPage(OrderLog orderLog, int page, int size);

    PageInfo<OrderLog> findPage(int page, int size);

    List<OrderLog> findList(OrderLog orderLog);

    void delete(String id);

    void update(OrderLog orderLog);

    void add(OrderLog orderLog);

    OrderLog findById(String id);

    List<OrderLog> findAll();

    //根据用户名和订单号查询日志信息
    OrderLog queryOrderLog(String username, String orderId);

}
