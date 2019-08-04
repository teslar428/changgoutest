package com.changgou.goods.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    //条件分页查询
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Album album, @PathVariable("page") int page, @PathVariable("size") int size) {
        PageInfo<Album> pageInfo = albumService.findPage(album, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    //分页查询
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        PageInfo<Album> pageInfo = albumService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    //多条件搜索
    @PostMapping("/search")
    public Result<List<Album>> findList(@RequestBody(required = false) Album album) {
        List<Album> list = albumService.findList(album);
        return new Result<List<Album>>(true, StatusCode.OK, "查询成功", list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id) {
        albumService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }


    @PutMapping("/{id}")
    public Result update(@RequestBody Album album, @PathVariable("id") Long id) {
        album.setId(id);
        albumService.update(album);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @PostMapping
    public Result add(@RequestBody Album album) {
        albumService.add(album);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    @GetMapping("/{id}")
    public Result<Album> findById(@PathVariable("id") Long id) {
        Album album = albumService.findById(id);
        return new Result<Album>(true, StatusCode.OK, "查询成功", album);
    }

    @GetMapping
    public Result<Album> findAll() {
        List<Album> list = albumService.findAll();
        return new Result<Album>(true, StatusCode.OK, "查询成功", list);
    }
}
