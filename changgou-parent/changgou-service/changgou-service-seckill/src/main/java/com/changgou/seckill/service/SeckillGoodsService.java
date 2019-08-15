package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillGoods;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SeckillGoodsService {

    PageInfo<SeckillGoods> findPage(SeckillGoods seckillGoods, int page, int size);

    PageInfo<SeckillGoods> findPage(int page, int size);

    List<SeckillGoods> findList(SeckillGoods seckillGoods);

    void delete(Long id);

    void update(SeckillGoods seckillGoods);

    void add(SeckillGoods seckillGoods);

    SeckillGoods findById(Long id);

    List<SeckillGoods> findAll();

    List<SeckillGoods> list(String key);

    SeckillGoods one(String time, Long id);
}
