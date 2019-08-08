package com.changgou.user.service;

import com.changgou.user.pojo.Address;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AddressService {

    PageInfo<Address> findPage(Address address, int page, int size);

    PageInfo<Address> findPage(int page, int size);

    List<Address> findList(Address address);

    void delete(Integer id);

    void update(Address address);

    void add(Address address);

    Address findById(Integer id);

    List<Address> findAll();
}
