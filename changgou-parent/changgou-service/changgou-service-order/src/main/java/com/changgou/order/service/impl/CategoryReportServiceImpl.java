package com.changgou.order.service.impl;

import com.changgou.order.dao.CategoryReportMapper;
import com.changgou.order.pojo.CategoryReport;
import com.changgou.order.service.CategoryReportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class CategoryReportServiceImpl implements CategoryReportService {

    @Autowired
    private CategoryReportMapper categoryReportMapper;

    // CategoryReport条件+分页查询
    @Override
    public PageInfo<CategoryReport> findPage(CategoryReport categoryReport, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(categoryReport);
        //执行搜索
        return new PageInfo<CategoryReport>(categoryReportMapper.selectByExample(example));
    }

    // CategoryReport分页查询
    @Override
    public PageInfo<CategoryReport> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<CategoryReport>(categoryReportMapper.selectAll());
    }

    // CategoryReport条件查询
    @Override
    public List<CategoryReport> findList(CategoryReport categoryReport){
        //构建查询条件
        Example example = createExample(categoryReport);
        //根据构建的条件查询数据
        return categoryReportMapper.selectByExample(example);
    }

    // CategoryReport构建查询对象
    public Example createExample(CategoryReport categoryReport){
        Example example=new Example(CategoryReport.class);
        Example.Criteria criteria = example.createCriteria();
        if(categoryReport!=null){
            // 1级分类
            if(!StringUtils.isEmpty(categoryReport.getCategoryId1())){
                    criteria.andEqualTo("categoryId1",categoryReport.getCategoryId1());
            }
            // 2级分类
            if(!StringUtils.isEmpty(categoryReport.getCategoryId2())){
                    criteria.andEqualTo("categoryId2",categoryReport.getCategoryId2());
            }
            // 3级分类
            if(!StringUtils.isEmpty(categoryReport.getCategoryId3())){
                    criteria.andEqualTo("categoryId3",categoryReport.getCategoryId3());
            }
            // 统计日期
            if(!StringUtils.isEmpty(categoryReport.getCountDate())){
                    criteria.andEqualTo("countDate",categoryReport.getCountDate());
            }
            // 销售数量
            if(!StringUtils.isEmpty(categoryReport.getNum())){
                    criteria.andEqualTo("num",categoryReport.getNum());
            }
            // 销售额
            if(!StringUtils.isEmpty(categoryReport.getMoney())){
                    criteria.andEqualTo("money",categoryReport.getMoney());
            }
        }
        return example;
    }

    // 删除
    @Override
    public void delete(Date id){
        categoryReportMapper.deleteByPrimaryKey(id);
    }

    // 修改CategoryReport
    @Override
    public void update(CategoryReport categoryReport){
        categoryReportMapper.updateByPrimaryKeySelective(categoryReport);
    }

    // 增加CategoryReport
    @Override
    public void add(CategoryReport categoryReport){
        categoryReportMapper.insertSelective(categoryReport);
    }

    // 根据ID查询CategoryReport
    @Override
    public CategoryReport findById(Date id){
        return  categoryReportMapper.selectByPrimaryKey(id);
    }

    // 查询CategoryReport全部数据
    @Override
    public List<CategoryReport> findAll() {
        return categoryReportMapper.selectAll();
    }
}
