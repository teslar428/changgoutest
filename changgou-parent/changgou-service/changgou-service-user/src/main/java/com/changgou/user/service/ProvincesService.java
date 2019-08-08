package com.changgou.user.service;

import com.changgou.user.pojo.Provinces;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProvincesService {

    PageInfo<Provinces> findPage(Provinces provinces, int page, int size);

    PageInfo<Provinces> findPage(int page, int size);

    List<Provinces> findList(Provinces provinces);

    void delete(String id);

    void update(Provinces provinces);

    void add(Provinces provinces);

    Provinces findById(String id);

    List<Provinces> findAll();
}
