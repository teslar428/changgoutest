package com.changgou.pay;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class WeixinPayApplication {
    @Autowired
    private Environment environment;

    //创建DirectExchange交换机
    @Bean
    public DirectExchange basicExchange() {
        return new DirectExchange(environment.getProperty("mq.pay.exchange.order"), true, false);
    }

    //创建队列
    @Bean(name = "queueOrder")
    public Queue queueOrder() {
        return new Queue(environment.getProperty("mq.pay.queue.order"), true);
    }

    //队列绑定到交换机上
    @Bean
    public Binding basicBinding() {
        return BindingBuilder.bind(queueOrder()).to(basicExchange()).with(environment.getProperty("mq.pay.routing.key"));
    }

    public static void main(String[] args) {
        SpringApplication.run(WeixinPayApplication.class, args);
    }
}