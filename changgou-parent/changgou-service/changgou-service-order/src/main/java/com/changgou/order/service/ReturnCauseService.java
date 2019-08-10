package com.changgou.order.service;

import com.changgou.order.pojo.ReturnCause;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ReturnCauseService {

    PageInfo<ReturnCause> findPage(ReturnCause returnCause, int page, int size);

    PageInfo<ReturnCause> findPage(int page, int size);

    List<ReturnCause> findList(ReturnCause returnCause);

    void delete(Integer id);

    void update(ReturnCause returnCause);

    void add(ReturnCause returnCause);

    ReturnCause findById(Integer id);

    List<ReturnCause> findAll();
}
