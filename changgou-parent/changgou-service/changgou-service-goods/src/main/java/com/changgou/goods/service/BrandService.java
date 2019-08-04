package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BrandService {
    List<Brand> findAll();

    Brand findById(Integer id);

    void add(Brand brand);

    void update(Brand brand);

    void delete(Integer id);

    List<Brand> findList(Brand brand);

    PageInfo<Brand> findPage(int page, int size);

    PageInfo<Brand> findPage(Brand brand, int page, int size);

    List<Brand> findByCategory(Integer categoryId);
}
