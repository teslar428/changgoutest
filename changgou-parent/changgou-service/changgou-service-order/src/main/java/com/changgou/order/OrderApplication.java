package com.changgou.order;

import com.changgou.entity.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.changgou.goods.feign")
@MapperScan(basePackages = {"com.changgou.order.dao"})
public class OrderApplication {
    @Autowired
    private TokenDecode tokenDecode;

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}