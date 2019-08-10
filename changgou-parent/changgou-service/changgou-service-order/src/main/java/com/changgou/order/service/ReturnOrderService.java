package com.changgou.order.service;

import com.changgou.order.pojo.ReturnOrder;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ReturnOrderService {

    PageInfo<ReturnOrder> findPage(ReturnOrder returnOrder, int page, int size);

    PageInfo<ReturnOrder> findPage(int page, int size);

    List<ReturnOrder> findList(ReturnOrder returnOrder);

    void delete(Long id);

    void update(ReturnOrder returnOrder);

    void add(ReturnOrder returnOrder);

    ReturnOrder findById(Long id);

    List<ReturnOrder> findAll();
}
