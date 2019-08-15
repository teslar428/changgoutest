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
    public void consumeMessage(@Payload String message) {
        System.out.println(message);
        Map<String, String> resuleMap = JSON.parseObject(message, Map.class);
        System.out.println("监听到的消息:" + resuleMap);

        String returnCode = resuleMap.get("return_code");
        String resultCode = resuleMap.get("result_code");
        if (returnCode.equalsIgnoreCase("success")) {
            String outTradeNo = resuleMap.get("out_trade_no");
            Map<String, String> attachMap = JSON.parseObject(resuleMap.get("attach"), Map.class);
            if (resultCode.equalsIgnoreCase("success")) {
                seckillOrderService.updatePayStatus(outTradeNo, resuleMap.get("transaction_id"), attachMap.get("username"));
            } else {
                //支付失败,删除订单
                seckillOrderService.closeOrder(attachMap.get("username"));
            }
        }
    }
}
