package com.changgou.content.service.impl;

import com.changgou.content.dao.ContentCategoryMapper;
import com.changgou.content.pojo.ContentCategory;
import com.changgou.content.service.ContentCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    // ContentCategory条件+分页查询
    @Override
    public PageInfo<ContentCategory> findPage(ContentCategory contentCategory, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(contentCategory);
        //执行搜索
        return new PageInfo<ContentCategory>(contentCategoryMapper.selectByExample(example));
    }

    // ContentCategory分页查询
    @Override
    public PageInfo<ContentCategory> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<ContentCategory>(contentCategoryMapper.selectAll());
    }

    // ContentCategory条件查询
    @Override
    public List<ContentCategory> findList(ContentCategory contentCategory){
        //构建查询条件
        Example example = createExample(contentCategory);
        //根据构建的条件查询数据
        return contentCategoryMapper.selectByExample(example);
    }

    // ContentCategory构建查询对象
    public Example createExample(ContentCategory contentCategory){
        Example example=new Example(ContentCategory.class);
        Example.Criteria criteria = example.createCriteria();
        if(contentCategory!=null){
            // 类目ID
            if(!StringUtils.isEmpty(contentCategory.getId())){
                    criteria.andEqualTo("id",contentCategory.getId());
            }
            // 分类名称
            if(!StringUtils.isEmpty(contentCategory.getName())){
                    criteria.andLike("name","%"+contentCategory.getName()+"%");
            }
        }
        return example;
    }

    // 删除
    @Override
    public void delete(Long id){
        contentCategoryMapper.deleteByPrimaryKey(id);
    }

    // 修改ContentCategory
    @Override
    public void update(ContentCategory contentCategory){
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
    }

    // 增加ContentCategory
    @Override
    public void add(ContentCategory contentCategory){
        contentCategoryMapper.insertSelective(contentCategory);
    }

    // 根据ID查询ContentCategory
    @Override
    public ContentCategory findById(Long id){
        return  contentCategoryMapper.selectByPrimaryKey(id);
    }

    // 查询ContentCategory全部数据
    @Override
    public List<ContentCategory> findAll() {
        return contentCategoryMapper.selectAll();
    }
}
