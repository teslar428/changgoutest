package com.changgou.seckill.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.pay.feign.WeixinPayFeign;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.queue.seckillordertimer}")
public class SeckillOrderDelayMessageListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private WeixinPayFeign weixinPayFeign;

    @RabbitHandler
    public void consumeMessage(@Payload String message) {
        SeckillStatus seckillStatus = JSON.parseObject(message, SeckillStatus.class);
        String username = seckillStatus.getUsername();
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
        if (seckillOrder != null) {//说明用户未支付
            Result result = weixinPayFeign.closePay(seckillOrder.getId());
            Map<String, String> map = (Map<String, String>) result.getData();
            if (map != null && map.get("return_code").equalsIgnoreCase("success") && map.get("result_code").equalsIgnoreCase("success")) {
                seckillOrderService.closeOrder(username);
            }
        }
    }
}
