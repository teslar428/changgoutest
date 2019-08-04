package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public Example createExample(Category category) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if (category != null) {
            // 分类ID
            if (!StringUtils.isEmpty(category.getId())) {
                criteria.andEqualTo("id", category.getId());
            }
            // 分类名称
            if (!StringUtils.isEmpty(category.getName())) {
                criteria.andLike("name", "%" + category.getName() + "%");
            }
            // 商品数量
            if (!StringUtils.isEmpty(category.getGoodsNum())) {
                criteria.andEqualTo("goodsNum", category.getGoodsNum());
            }
            // 是否显示
            if (!StringUtils.isEmpty(category.getIsShow())) {
                criteria.andEqualTo("isShow", category.getIsShow());
            }
            // 是否导航
            if (!StringUtils.isEmpty(category.getIsMenu())) {
                criteria.andEqualTo("isMenu", category.getIsMenu());
            }
            // 排序
            if (!StringUtils.isEmpty(category.getSeq())) {
                criteria.andEqualTo("seq", category.getSeq());
            }
            // 上级ID
            if (!StringUtils.isEmpty(category.getParentId())) {
                criteria.andEqualTo("parentId", category.getParentId());
            }
            // 模板ID
            if (!StringUtils.isEmpty(category.getTemplateId())) {
                criteria.andEqualTo("templateId", category.getTemplateId());
            }
        }
        return example;
    }

    public PageInfo<Category> findPage(Category category, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(category);
        return new PageInfo<Category>(categoryMapper.selectByExample(example));
    }

    public PageInfo<Category> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Category>(categoryMapper.selectAll());
    }

    public List<Category> findList(Category category) {
        Example example = createExample(category);
        return categoryMapper.selectByExample(example);
    }

    public void delete(Integer id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    public void update(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    public void add(Category category) {
        categoryMapper.insertSelective(category);
    }

    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    public List<Category> findByParentId(Integer pid) {
        Category category = new Category();
        category.setId(pid);
        return categoryMapper.select(category);
    }
}
