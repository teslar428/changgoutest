package com.changgou.user.service;

import com.changgou.user.pojo.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {

    PageInfo<User> findPage(User user, int page, int size);

    PageInfo<User> findPage(int page, int size);

    List<User> findList(User user);

    void delete(String id);

    void update(User user);

    void add(User user);

    User findById(String id);

    List<User> findAll();

    //增加积分
    int addUserPoints(String username,Integer point);
}
