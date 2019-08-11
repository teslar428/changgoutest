package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    private CartFeign cartFeign;

    @Value("${staticLink}")
    private String staticLink;

    @RequestMapping("/list")
    public String list(Model model) {
        Result<List<OrderItem>> result = cartFeign.list();
        model.addAttribute("items", result.getData());
        model.addAttribute("staticLink",staticLink);
        return "cart";
    }

    @ResponseBody
    @GetMapping("/json/add")
    public Result<List<OrderItem>> add(@RequestParam("num") Integer num, @RequestParam("id") Long id) {
        cartFeign.add(num, id);
        Result<List<OrderItem>> cartResult = cartFeign.list();
        return cartResult;
    }
}
