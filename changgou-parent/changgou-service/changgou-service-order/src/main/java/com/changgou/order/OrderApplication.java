package com.changgou.order;

import com.changgou.interceptor.FeignInterceptor;
import com.changgou.entity.IdWorker;
import com.changgou.entity.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.goods.feign","com.changgou.user.feign","com.changgou.pay.feign"})
@MapperScan(basePackages = {"com.changgou.order.dao"})
@EnableScheduling
public class OrderApplication {
    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1,1);
    }

    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}