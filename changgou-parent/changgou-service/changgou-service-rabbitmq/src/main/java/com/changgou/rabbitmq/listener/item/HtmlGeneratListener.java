package com.changgou.rabbitmq.listener.item;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Message;
import com.changgou.item.feign.PageFeign;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RabbitListener(queues = "topic.queue.spu")
public class HtmlGeneratListener {
    @Autowired
    private PageFeign pageFeign;

    //生成静态页
    @RabbitHandler
    public void getInfo(String msg) {
        Message message = JSON.parseObject(msg, Message.class);
        Long id = Long.parseLong(message.getContent().toString());
        if (message.getCode() == 2) {
            pageFeign.createHtml(id);
        }
        if (message.getCode() == 3) {
            pageFeign.deleteHtml(id);
        }
    }
}
