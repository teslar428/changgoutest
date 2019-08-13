package com.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "pay")
@RequestMapping("/weixin/pay")
public interface WeixinPayFeign {
    @GetMapping("/status/query")
    Result queryStatus(String orderId);
}
