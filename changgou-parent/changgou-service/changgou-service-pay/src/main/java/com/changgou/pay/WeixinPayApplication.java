package com.changgou.pay;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class WeixinPayApplication {
    @Autowired
    private Environment environment;

    @Bean
    public DirectExchange basicExchange(){
        return new DirectExchange(environment.getProperty("mq.pay.exchange.order"), true,false);
    }

    @Bean(name = "queueOrder")
    public Queue queueOrder() {
        return new Queue(environment.getProperty("mq.pay.queue.order"), true);
    }

    @Bean(name = "queueSeckillOrder")
    public Queue queueSeckillOrder() {
        return new Queue(environment.getProperty("mq.pay.queue.seckillorder"), true);
    }

    @Bean
    public Binding basicBindingOrder() {
        return BindingBuilder
                .bind(queueOrder())
                .to(basicExchange())
                .with(environment.getProperty("mq.pay.routing.orderkey"));
    }

    @Bean
    public Binding basicBindingSeckillOrder() {
        return BindingBuilder
                .bind(queueSeckillOrder())
                .to(basicExchange())
                .with(environment.getProperty("mq.pay.routing.seckillorderkey"));
    }

    public static void main(String[] args) {
        SpringApplication.run(WeixinPayApplication.class, args);
    }
}