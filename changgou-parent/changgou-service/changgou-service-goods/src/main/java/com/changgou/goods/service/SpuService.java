package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SpuService {

    PageInfo<Spu> findPage(Spu spu, int page, int size);

    PageInfo<Spu> findPage(int page, int size);

    List<Spu> findList(Spu spu);

    void delete(Long id);

    void update(Spu spu);

    void add(Spu spu);

    Spu findById(Long id);

    List<Spu> findAll();

    void saveGoods(Goods goods);

    Goods findGoodsById(Long spuId);

    void audit(Long spuId);

    void pull(Long spuId);

    void put(Long spuId);

    int putMany(Long[] ids);

    int pullMany(Long[] ids);

    void logicDelete(Long spuId);

    void restore(Long spuId);
}
