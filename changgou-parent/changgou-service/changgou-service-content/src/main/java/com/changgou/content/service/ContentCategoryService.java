package com.changgou.content.service;

import com.changgou.content.pojo.ContentCategory;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ContentCategoryService {

    PageInfo<ContentCategory> findPage(ContentCategory contentCategory, int page, int size);

    PageInfo<ContentCategory> findPage(int page, int size);

    List<ContentCategory> findList(ContentCategory contentCategory);

    void delete(Long id);

    void update(ContentCategory contentCategory);

    void add(ContentCategory contentCategory);

    ContentCategory findById(Long id);

    List<ContentCategory> findAll();
}
