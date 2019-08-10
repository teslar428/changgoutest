package com.changgou.order.service;

import com.changgou.order.pojo.Preferential;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PreferentialService {

    PageInfo<Preferential> findPage(Preferential preferential, int page, int size);

    PageInfo<Preferential> findPage(int page, int size);

    List<Preferential> findList(Preferential preferential);

    void delete(Integer id);

    void update(Preferential preferential);

    void add(Preferential preferential);

    Preferential findById(Integer id);

    List<Preferential> findAll();
}
