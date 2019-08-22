package com.changgou.seckill.task;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.IdWorker;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MultiThreadingCreateOrder {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment environment;

    @Async
    public void createOrder() {
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();
        try {
            if (seckillStatus != null) {
                Object sgood = redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).rightPop();
                if (sgood == null) {//商品已售罄
                    clearQueue(seckillStatus);
                    return;
                }

                String time = seckillStatus.getTime();
                String username = seckillStatus.getUsername();
                Long id = seckillStatus.getGoodsId();

                SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + time).get(id);

                //如果有库存,则创建秒杀订单
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(idWorker.nextId());
                seckillOrder.setSeckillId(id);
                seckillOrder.setMoney(seckillGoods.getCostPrice());
                seckillOrder.setUserId(username);
                seckillOrder.setCreateTime(new Date());
                seckillOrder.setStatus("0");

                redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);

                //库存减少
                Long seckillGoodsCount = redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillGoods.getId(), -1);
                seckillGoods.setStockCount(seckillGoodsCount.intValue());
                if (seckillGoods.getStockCount() <= 0) {//没有库存
                    seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                    redisTemplate.boundHashOps("SeckillGoods_" + time).delete(id);
                } else {
                    redisTemplate.boundHashOps("SeckillGoods_" + time).put(id, seckillGoods);
                }

                seckillStatus.setStatus(2);
                seckillStatus.setOrderId(seckillOrder.getId());
                seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
                redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
                sendTimerMessage(seckillStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearQueue(SeckillStatus seckillStatus) {
        String username = seckillStatus.getUsername();
        redisTemplate.boundHashOps("UserQueueCount").delete(username);
        redisTemplate.boundHashOps("UserQueueStatus").delete(username);
    }

    //发送延时消息到RabbitMQ中
    public void sendTimerMessage(SeckillStatus seckillStatus) {
        rabbitTemplate.convertAndSend(environment.getProperty("mq.pay.queue.seckillordertimerdelay"), (Object) JSON.toJSONString(seckillStatus), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
    }
}
