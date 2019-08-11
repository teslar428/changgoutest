package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {

    //根据ID查询用户信息
    @GetMapping("/load/{id}")
    Result<User> findById(@PathVariable("id") String id);

    @GetMapping("/points/add")
    Result addPoints(@RequestParam("points")Integer points);
}