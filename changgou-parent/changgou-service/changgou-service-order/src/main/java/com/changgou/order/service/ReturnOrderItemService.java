package com.changgou.order.service;

import com.changgou.order.pojo.ReturnOrderItem;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ReturnOrderItemService {

    PageInfo<ReturnOrderItem> findPage(ReturnOrderItem returnOrderItem, int page, int size);

    PageInfo<ReturnOrderItem> findPage(int page, int size);

    List<ReturnOrderItem> findList(ReturnOrderItem returnOrderItem);

    void delete(Long id);

    void update(ReturnOrderItem returnOrderItem);

    void add(ReturnOrderItem returnOrderItem);

    ReturnOrderItem findById(Long id);

    List<ReturnOrderItem> findAll();
}
