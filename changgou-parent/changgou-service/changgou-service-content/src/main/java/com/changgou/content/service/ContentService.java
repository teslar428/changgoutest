package com.changgou.content.service;

import com.changgou.content.pojo.Content;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ContentService {

    PageInfo<Content> findPage(Content content, int page, int size);

    PageInfo<Content> findPage(int page, int size);

    List<Content> findList(Content content);

    void delete(Long id);

    void update(Content content);

    void add(Content content);

    Content findById(Long id);

    List<Content> findAll();

    List<Content> findByCategory(Long id);
}
