package com.changgou.rabbitmqdelay.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    public static final String QUEUE_MESSAGE = "queue.message";

    public static final String DLX_EXCHANGE = "dlx.exchange";

    public static final String QUEUE_MESSAGE_DELAY = "queue.message.delay";

    @Bean
    public Queue messageQueue() {
        return new Queue(QUEUE_MESSAGE, true);
    }

    @Bean
    public Queue delayMessageQueue() {
        return QueueBuilder.durable(QUEUE_MESSAGE_DELAY)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)        // 消息超时进入死信队列，绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key", QUEUE_MESSAGE)   // 绑定指定的routing-key
                .build();
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Binding basicBinding(Queue messageQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(messageQueue)
                .to(directExchange)
                .with(QUEUE_MESSAGE);
    }
}
