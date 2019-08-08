package com.changgou.item.service;

public interface PageService {
    //根据商品ID生成静态页
    void createPageHtml(Long spuId);

    void deletePageHtml(Long spuId);
}
