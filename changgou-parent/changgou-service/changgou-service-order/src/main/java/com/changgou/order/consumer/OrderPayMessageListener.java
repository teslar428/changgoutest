package com.changgou.order.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.order.pojo.OrderLog;
import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class OrderPayMessageListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void consumeMessage(@Payload String message) {
        Map<String, String> resultMap = JSON.parseObject(message, Map.class);

        String returnCode = resultMap.get("return_code");
        String resultCode = resultMap.get("result_code");
        if (returnCode.equalsIgnoreCase("success")) {
            String outTradeNo = resultMap.get("out_trade_no");
            //获取订单日志
            OrderLog orderLog = (OrderLog) redisTemplate.boundHashOps("OrderLog").get(outTradeNo);
            if (resultCode.equalsIgnoreCase("success")) {//支付成功
                orderService.updateStatus(orderLog.getUsername(), orderLog.getOrderId(), resultMap.get("transaction_id"), orderLog);
            } else {//支付失败
                orderService.deleteOrder(orderLog);
            }
        }
    }

}
