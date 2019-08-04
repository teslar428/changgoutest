package com.changgou.goods.service;

import com.changgou.goods.pojo.Category;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryService {

    PageInfo<Category> findPage(Category category, int page, int size);

    PageInfo<Category> findPage(int page, int size);

    List<Category> findList(Category category);

    void delete(Integer id);

    void update(Category category);

    void add(Category category);

    Category findById(Integer id);

    List<Category> findAll();

    List<Category> findByParentId(Integer pid);
}
