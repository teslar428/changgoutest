package com.changgou.order.service;

import com.changgou.order.pojo.OrderConfig;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderConfigService {

    PageInfo<OrderConfig> findPage(OrderConfig orderConfig, int page, int size);

    PageInfo<OrderConfig> findPage(int page, int size);

    List<OrderConfig> findList(OrderConfig orderConfig);

    void delete(Integer id);

    void update(OrderConfig orderConfig);

    void add(OrderConfig orderConfig);

    OrderConfig findById(Integer id);

    List<OrderConfig> findAll();
}
