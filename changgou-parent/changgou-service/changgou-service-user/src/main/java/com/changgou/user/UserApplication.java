package com.changgou.user;

import com.changgou.entity.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.changgou.user.dao")
public class UserApplication {
    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
