package com.changgou.goods.service.impl;

import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplateMapper templateMapper;

    public Example createExample(Template template) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (template != null) {
            // ID
            if (!StringUtils.isEmpty(template.getId())) {
                criteria.andEqualTo("id", template.getId());
            }
            // 模板名称
            if (!StringUtils.isEmpty(template.getName())) {
                criteria.andLike("name", "%" + template.getName() + "%");
            }
            // 规格数量
            if (!StringUtils.isEmpty(template.getSpecNum())) {
                criteria.andEqualTo("specNum", template.getSpecNum());
            }
            // 参数数量
            if (!StringUtils.isEmpty(template.getParaNum())) {
                criteria.andEqualTo("paraNum", template.getParaNum());
            }
        }
        return example;
    }

    public PageInfo<Template> findPage(Template template, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(template);
        return new PageInfo<Template>(templateMapper.selectByExample(example));
    }

    public PageInfo<Template> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Template>(templateMapper.selectAll());
    }

    public List<Template> findList(Template template) {
        Example example = createExample(template);
        return templateMapper.selectByExample(example);
    }

    public void delete(Integer id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    public void update(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
    }

    public void add(Template template) {
        templateMapper.insertSelective(template);
    }

    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    public List<Template> findAll() {
        return templateMapper.selectAll();
    }
}
