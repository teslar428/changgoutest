package com.changgou.order;

import com.changgou.entity.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.changgou.order.feign")
public class OrderWebApplication {
    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderWebApplication.class, args);
    }
}
