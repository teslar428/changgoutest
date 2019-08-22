package com.changgou.seckill.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.feign.WeixinPayFeign;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.queue.seckillorder}")
public class SeckillOrderPayMessageListener {
    @Autowired
    private SeckillOrderService seckillOrderService;

    @RabbitHandler
    public void consumeMessage(@Payload String message) throws Exception{
        Map<String, String> resuleMap = JSON.parseObject(message, Map.class);

        String returnCode = resuleMap.get("return_code");
        String resultCode = resuleMap.get("result_code");
        if (returnCode.equalsIgnoreCase("success")) {
            Map<String, String> attachMap = JSON.parseObject(resuleMap.get("attach"), Map.class);
            String username = attachMap.get("username");
            if (resultCode.equalsIgnoreCase("success")) {
                String transaction_id = resuleMap.get("transaction_id");
                String time_end = resuleMap.get("time_end");
                seckillOrderService.updatePayStatus(username, time_end, transaction_id);
            } else {
                //支付失败,删除订单
                seckillOrderService.closeOrder(username);
            }
        }
    }
}
