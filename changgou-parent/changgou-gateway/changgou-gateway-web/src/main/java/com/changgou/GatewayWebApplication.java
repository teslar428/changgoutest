package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableEurekaClient
public class GatewayWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayWebApplication.class, args);
    }

    //ip限流
    @Bean(name = "ipKeyResolver")
    public KeyResolver userKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //IP限流
                String hostName = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                System.out.println("hostName:"+hostName);
                return Mono.just(hostName);

                //用户限流
//                String userId = exchange.getRequest().getQueryParams().getFirst("userId");
//                return Mono.just(userId);

                //接口限流
//                String value = exchange.getRequest().getPath().value();
//                return Mono.just(value);
            }
        };
    }
}
