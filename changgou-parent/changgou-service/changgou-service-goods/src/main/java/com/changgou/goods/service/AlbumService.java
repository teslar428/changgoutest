package com.changgou.goods.service;

import com.changgou.goods.pojo.Album;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AlbumService {
    //多条件分页查询
    PageInfo<Album> findPage(Album album, int page, int size);

    //分页查询
    PageInfo<Album> findPage(int page, int sizee);

    //多条件搜索
    List<Album> findList(Album album);

    void delete(Long id);

    void update(Album album);

    void add(Album album);

    Album findById(Long id);

    List<Album> findAll();
}
