package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.entity.TokenDecode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    /***
     * 加入购物车
     * @param num:购买的数量
     * @param id：购买的商品(SKU)ID
     * @return
     */
    @GetMapping("/add")
    public Result add(@RequestParam("num") Integer num, @RequestParam("id") Long id) {
        String username = tokenDecode.getUserInfo().get("username");
        cartService.add(num, id, username);
        return new Result(true, StatusCode.OK, "成功加入购物车");
    }

    //查询用户购物车列表
    @GetMapping("/list")
    public Result list() {
        String username = tokenDecode.getUserInfo().get("username");
        List<OrderItem> orderItemList = cartService.list(username);
        return new Result(true, StatusCode.OK, "购物车列表查询成功", orderItemList);
    }
}
