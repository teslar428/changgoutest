package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.*;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenDecode tokenDecode;

    // User分页条件搜索实现
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) User user, @PathVariable("page") int page, @PathVariable("size") int size) {
        //调用UserService实现分页条件查询User
        PageInfo<User> pageInfo = userService.findPage(user, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // User分页搜索实现
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        //调用UserService实现分页查询User
        PageInfo<User> pageInfo = userService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    // 多条件搜索数据
    @PostMapping("/search")
    public Result<List<User>> findList(@RequestBody(required = false) User user) {
        //调用UserService实现条件查询User
        List<User> list = userService.findList(user);
        return new Result<List<User>>(true, StatusCode.OK, "查询成功", list);
    }

    // 根据ID删除数据
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        //调用UserService实现根据主键删除
        userService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    // 修改User数据
    @PutMapping("/{id}")
    public Result update(@RequestBody User user, @PathVariable("id") String id) {
        //设置主键值
        user.setUsername(id);
        //调用UserService实现修改User
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    // 新增User数据
    @PostMapping
    public Result add(@RequestBody User user) {
        //调用UserService实现添加User
        userService.add(user);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    // 根据ID查询User数据
    @GetMapping({"/{id}","/load/{id}"})
    public Result<User> findById(@PathVariable("id") String id) {
        //调用UserService实现根据主键查询User
        User user = userService.findById(id);
        return new Result<User>(true, StatusCode.OK, "查询成功", user);
    }

    // 查询User全部数据
    @GetMapping
    public Result<List<User>> findAll(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        System.out.println("令牌信息:" + authorization);

        //调用UserService实现查询所有User
        List<User> list = userService.findAll();
        return new Result<List<User>>(true, StatusCode.OK, "查询成功", list);
    }

    @RequestMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        User user = userService.findById(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("role", "USER");
            info.put("success", "SUCCESS");
            info.put("username", username);
            //生成令牌
            String jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(info), null);

            Cookie cookie = new Cookie("Authorization", jwt);
            response.addCookie(cookie);
            return new Result(true, StatusCode.OK, "登录成功", jwt);
        }
        return new Result(false, StatusCode.LOGINERROR, "账号或密码错误");
    }

    //增加积分
    @GetMapping(value = "/points/add")
    public Result addPoints(Integer points){
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");
        userService.addUserPoints(username,points );
        return new Result(true,StatusCode.OK,"添加积分成功");
    }
}
