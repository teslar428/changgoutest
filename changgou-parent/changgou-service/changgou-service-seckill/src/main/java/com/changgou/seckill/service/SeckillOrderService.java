package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SeckillOrderService {

    PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size);

    PageInfo<SeckillOrder> findPage(int page, int size);

    List<SeckillOrder> findList(SeckillOrder seckillOrder);

    void delete(Long id);

    void update(SeckillOrder seckillOrder);

    void add(SeckillOrder seckillOrder);

    SeckillOrder findById(Long id);

    List<SeckillOrder> findAll();

    Boolean add(Long id, String time, String username);

    SeckillStatus queryStatus(String username);

    void updatePayStatus(String username, String time_end, String transaction_id) throws Exception;

    void closeOrder(String username);
}
