package com.changgou.user.service.impl;

import com.changgou.user.dao.ProvincesMapper;
import com.changgou.user.pojo.Provinces;
import com.changgou.user.service.ProvincesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ProvincesServiceImpl implements ProvincesService {

    @Autowired
    private ProvincesMapper provincesMapper;

    // Provinces条件+分页查询
    @Override
    public PageInfo<Provinces> findPage(Provinces provinces, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(provinces);
        //执行搜索
        return new PageInfo<Provinces>(provincesMapper.selectByExample(example));
    }

    // Provinces分页查询
    @Override
    public PageInfo<Provinces> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Provinces>(provincesMapper.selectAll());
    }

    // Provinces条件查询
    @Override
    public List<Provinces> findList(Provinces provinces){
        //构建查询条件
        Example example = createExample(provinces);
        //根据构建的条件查询数据
        return provincesMapper.selectByExample(example);
    }

    // Provinces构建查询对象
    public Example createExample(Provinces provinces){
        Example example=new Example(Provinces.class);
        Example.Criteria criteria = example.createCriteria();
        if(provinces!=null){
            // 省份ID
            if(!StringUtils.isEmpty(provinces.getProvinceid())){
                    criteria.andEqualTo("provinceid",provinces.getProvinceid());
            }
            // 省份名称
            if(!StringUtils.isEmpty(provinces.getProvince())){
                    criteria.andEqualTo("province",provinces.getProvince());
            }
        }
        return example;
    }

    // 删除
    @Override
    public void delete(String id){
        provincesMapper.deleteByPrimaryKey(id);
    }

    // 修改Provinces
    @Override
    public void update(Provinces provinces){
        provincesMapper.updateByPrimaryKeySelective(provinces);
    }

    // 增加Provinces
    @Override
    public void add(Provinces provinces){
        provincesMapper.insertSelective(provinces);
    }

    // 根据ID查询Provinces
    @Override
    public Provinces findById(String id){
        return  provincesMapper.selectByPrimaryKey(id);
    }

    // 查询Provinces全部数据
    @Override
    public List<Provinces> findAll() {
        return provincesMapper.selectAll();
    }
}
