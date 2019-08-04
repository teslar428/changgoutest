package com.changgou.goods.service;

import com.changgou.goods.pojo.CategoryBrand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryBrandService {

    PageInfo<CategoryBrand> findPage(CategoryBrand categoryBrand, int page, int size);

    PageInfo<CategoryBrand> findPage(int page, int size);

    List<CategoryBrand> findList(CategoryBrand categoryBrand);

    void delete(Integer id);

    void update(CategoryBrand categoryBrand);

    void add(CategoryBrand categoryBrand);

    CategoryBrand findById(Integer id);

    List<CategoryBrand> findAll();
}
