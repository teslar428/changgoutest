package com.changgou.seckill;

import com.changgou.entity.IdWorker;
import com.changgou.entity.TokenDecode;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.pay.feign"})
@MapperScan(basePackages = {"com.changgou.seckill.dao"})
@EnableScheduling
@EnableAsync
public class SeckillApplication {
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(2, 2);
    }

    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    @Autowired
    private Environment environment;

    @Bean
    public DirectExchange basicExchange() {
        return new DirectExchange(environment.getProperty("mq.pay.exchange.order"), true, false);
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
    public Binding basicBindingOrder(){
        return BindingBuilder
                .bind(queueOrder())
                .to(basicExchange())
                .with(environment.getProperty("mq.pay.routing.orderkey"));
    }

    @Bean
    public Binding basicBindingSeckillOrder(){
        return BindingBuilder
                .bind(queueSeckillOrder())
                .to(basicExchange())
                .with(environment.getProperty("mq.pay.routing.seckillorderkey"));
    }

    @Bean
    public Queue seckillOrderTimerQueue() {
        return new Queue(environment.getProperty("mq.pay.queue.seckillordertimer"), true);
    }

    @Bean
    public Queue delaySeckillOrderTimerQueue() {
        return QueueBuilder.durable(environment.getProperty("mq.pay.queue.seckillordertimerdelay"))
                .withArgument("x-dead-letter-exchange", environment.getProperty("mq.pay.exchange.order"))        // 消息超时进入死信队列，绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key", environment.getProperty("mq.pay.queue.seckillordertimer"))   // 绑定指定的routing-key
                .build();
    }

    @Bean
    public Binding basicBinding() {
        return BindingBuilder.bind(seckillOrderTimerQueue())
                .to(basicExchange())
                .with(environment.getProperty("mq.pay.queue.seckillordertimer"));
    }


    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
}
