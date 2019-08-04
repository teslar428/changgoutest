package com.changgou.goods.service;

import com.changgou.goods.pojo.Spec;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SpecService {
    PageInfo<Spec> findPage(Spec spec, int page, int size);

    PageInfo<Spec> findPage(int page, int size);

    List<Spec> findList(Spec spec);

    void delete(Integer id);

    void update(Spec spec);

    void add(Spec spec);

    Spec findById(Integer id);

    List<Spec> findAll();
}
