package com.changgou.goods.service;

import com.changgou.goods.pojo.Para;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ParaService {
    PageInfo<Para> findPage(Para para, int page, int size);

    PageInfo<Para> findPage(int page, int size);

    List<Para> findList(Para para);

    void delete(Integer id);

    void update(Para para);

    void add(Para para);

    Para findById(Integer id);

    List<Para> findAll();
}
