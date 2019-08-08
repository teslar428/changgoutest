package com.changgou.canal.mq.send;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //Topic消息发送
    public void sendMessage(Message message) {
        rabbitTemplate.convertAndSend(message.getExechange(), message.getRoutekey(), JSON.toJSONString(message));
    }
}
