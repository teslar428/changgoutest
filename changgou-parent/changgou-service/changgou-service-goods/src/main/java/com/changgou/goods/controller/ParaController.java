package com.changgou.goods.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.service.ParaService;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/para")
@CrossOrigin
public class ParaController {
    @Autowired
    private ParaService paraService;

    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Para para, @PathVariable("page") int page, @PathVariable("size") int size) {
        PageInfo<Para> pageInfo = paraService.findPage(para, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        PageInfo<Para> pageInfo = paraService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    @PostMapping("/search")
    public Result<List<Para>> findList(@RequestBody(required = false) Para para) {
        List<Para> list = paraService.findList(para);
        return new Result<List<Para>>(true, StatusCode.OK, "查询成功", list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        paraService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Integer id, @RequestBody Para para) {
        para.setId(id);
        paraService.update(para);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @PostMapping
    public Result add(@RequestBody Para para) {
        paraService.add(para);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    @GetMapping("/{id}")
    public Result<Para> findById(@PathVariable("id") Integer id) {
        Para para = paraService.findById(id);
        return new Result<Para>(true, StatusCode.OK, "查询成功", para);
    }

    @GetMapping
    public Result<Para> findAll() {
        List<Para> paraList = paraService.findAll();
        return new Result<Para>(true, StatusCode.OK, "查询成功", paraList);
    }

    @GetMapping("/category/{id}")
    public Result<List<Para>> getByCategoryId(@PathVariable("id") Integer id) {
        //根据分类ID查询对应的参数信息
        List<Para> paras = paraService.findByCategoryId(id);
        Result<List<Para>> result = new Result<List<Para>>(true, StatusCode.OK, "查询分类对应的品牌成功！", paras);
        return result;
    }
}
