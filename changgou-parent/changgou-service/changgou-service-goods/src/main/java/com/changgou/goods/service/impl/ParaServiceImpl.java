package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.ParaMapper;
import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.ParaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ParaServiceImpl implements ParaService {
    @Autowired
    private ParaMapper paraMapper;

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public Example createExample(Para para) {
        Example example = new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if (para != null) {
            // id
            if (!StringUtils.isEmpty(para.getId())) {
                criteria.andEqualTo("id", para.getId());
            }
            // 名称
            if (!StringUtils.isEmpty(para.getName())) {
                criteria.andLike("name", "%" + para.getName() + "%");
            }
            // 选项
            if (!StringUtils.isEmpty(para.getOptions())) {
                criteria.andEqualTo("options", para.getOptions());
            }
            // 排序
            if (!StringUtils.isEmpty(para.getSeq())) {
                criteria.andEqualTo("seq", para.getSeq());
            }
            // 模板ID
            if (!StringUtils.isEmpty(para.getTemplateId())) {
                criteria.andEqualTo("templateId", para.getTemplateId());
            }
        }
        return example;
    }

    public PageInfo<Para> findPage(Para para, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(para);
        return new PageInfo<Para>(paraMapper.selectByExample(example));
    }

    public PageInfo<Para> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Para>(paraMapper.selectAll());
    }

    public List<Para> findList(Para para) {
        Example example = createExample(para);
        return paraMapper.selectByExample(example);
    }

    public void updateParaNum(Para para, int count) {
        //修改模板数量统计
        Template template = templateMapper.selectByPrimaryKey(para.getTemplateId());
        template.setParaNum(template.getParaNum() + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }

    public void delete(Integer id) {
        Para para = paraMapper.selectByPrimaryKey(id);
        updateParaNum(para, -1);
        paraMapper.deleteByPrimaryKey(id);
    }

    public void update(Para para) {
        paraMapper.updateByPrimaryKeySelective(para);
    }

    public void add(Para para) {
        paraMapper.insertSelective(para);
        updateParaNum(para, 1);
    }

    public Para findById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    public List<Para> findAll() {
        return paraMapper.selectAll();
    }

    public List<Para> findByCategoryId(Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        Para para = new Para();
        para.setTemplateId(category.getTemplateId());
        return paraMapper.select(para);
    }
}
