package com.changgou.user.service;

import com.changgou.user.pojo.Cities;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CitiesService {

    PageInfo<Cities> findPage(Cities cities, int page, int size);

    PageInfo<Cities> findPage(int page, int size);

    List<Cities> findList(Cities cities);

    void delete(String id);

    void update(Cities cities);

    void add(Cities cities);

    Cities findById(String id);

    List<Cities> findAll();
}
