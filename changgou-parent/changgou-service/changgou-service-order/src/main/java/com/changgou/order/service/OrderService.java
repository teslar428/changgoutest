package com.changgou.order.service;

import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderLog;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderService {

    PageInfo<Order> findPage(Order order, int page, int size);

    PageInfo<Order> findPage(int page, int size);

    List<Order> findList(Order order);

    void delete(String id);

    void update(Order order);

    int add(Order order);

    Order findById(String id);

    List<Order> findAll();

    void updateStatus(String username, String orderId, String transactionId, OrderLog orderLog);

    void deleteOrder(OrderLog orderLog);
}
