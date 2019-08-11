package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/worder")
public class OrderController {
    @Autowired
    private AddressFeign addressFeign;

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private OrderFeign orderFeign;

    @Value("${staticLink}")
    private String staticLink;

    //订单结算信息查询
    @RequestMapping("/ready/order")
    public String readyOrder(Model model) {
        Result<List<Address>> addressResult = addressFeign.list();
        Result<List<OrderItem>> cartResult = cartFeign.list();

        //默认收件人信息
        for (Address address : addressResult.getData()) {
            if (address.getIsDefault().equals("1")) {
                model.addAttribute("deAddr", address);
                break;
            }
        }

        model.addAttribute("address", addressResult.getData());
        model.addAttribute("carts", cartResult.getData());
        model.addAttribute("staticLink", staticLink);
        return "order";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order){
        Result result = orderFeign.add(order);
        return  result;
    }
}
