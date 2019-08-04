package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.SpecService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecServiceImpl implements SpecService {
    @Autowired
    private SpecMapper specMapper;

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public Example createExample(Spec spec) {
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if (spec != null) {
            // ID
            if (!StringUtils.isEmpty(spec.getId())) {
                criteria.andEqualTo("id", spec.getId());
            }
            // 名称
            if (!StringUtils.isEmpty(spec.getName())) {
                criteria.andLike("name", "%" + spec.getName() + "%");
            }
            // 规格选项
            if (!StringUtils.isEmpty(spec.getOptions())) {
                criteria.andEqualTo("options", spec.getOptions());
            }
            // 排序
            if (!StringUtils.isEmpty(spec.getSeq())) {
                criteria.andEqualTo("seq", spec.getSeq());
            }
            // 模板ID
            if (!StringUtils.isEmpty(spec.getTemplateId())) {
                criteria.andEqualTo("templateId", spec.getTemplateId());
            }
        }
        return example;
    }

    public PageInfo<Spec> findPage(Spec spec, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(spec);
        return new PageInfo<Spec>(specMapper.selectByExample(example));
    }

    public PageInfo<Spec> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Spec>(specMapper.selectAll());
    }

    public List<Spec> findList(Spec spec) {
        Example example = createExample(spec);
        return specMapper.selectByExample(example);
    }

    //修改模板统计数据
    public void updateSpecNum(Spec spec, int count) {
        Template template = templateMapper.selectByPrimaryKey(spec.getTemplateId());
        template.setSpecNum(template.getParaNum() + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }

    public void delete(Integer id) {
        Spec spec = specMapper.selectByPrimaryKey(id);
        updateSpecNum(spec, -1);
        specMapper.deleteByPrimaryKey(id);
    }

    public void update(Spec spec) {
        specMapper.updateByPrimaryKeySelective(spec);
    }

    public void add(Spec spec) {
        specMapper.insertSelective(spec);
        updateSpecNum(spec, 1);
    }

    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    public List<Spec> findByCategoryId(Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        Spec spec = new Spec();
        spec.setTemplateId(category.getTemplateId());
        return specMapper.select(spec);
    }
}
