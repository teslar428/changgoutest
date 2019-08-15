package com.changgou.order.task;

import com.changgou.entity.Result;
import com.changgou.order.pojo.OrderLog;
import com.changgou.order.service.OrderService;
import com.changgou.pay.feign.WeixinPayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QueryPayStatus {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeixinPayFeign weixinPayFeign;

    @Scheduled(cron = "0/5 * * * * *")
    public void queryPay() {
        Object logId = redisTemplate.boundListOps("OrderLogList").rightPop();
        if (logId != null) {
            OrderLog orderLog = (OrderLog) redisTemplate.boundHashOps("OrderLog").get(logId);
            if (orderLog == null) {
                return;
            }
            Result result = weixinPayFeign.queryStatus(orderLog.getOrderId());
            Map<String, String> dataMap = (Map<String, String>) result.getData();
            String returnCode = dataMap.get("return_code");//网络通信标识
            String resultCode = dataMap.get("result_code");//业务返回代码
            String tradeState = dataMap.get("trade_state");//交易状态
            if (returnCode.equalsIgnoreCase("success") && resultCode.equalsIgnoreCase("success")) {
                if (tradeState.equalsIgnoreCase("success")) {//支付成功
                    orderService.updateStatus(orderLog.getUsername(), orderLog.getOrderId(), dataMap.get("transaction_id"), orderLog);
                } else {
                    if (tradeState.equalsIgnoreCase("notpay") || tradeState.equalsIgnoreCase("userpaying")) {
                        //未支付或正在支付,则将用户的支付信息存入队列中,后面再次调用查询状态
                        redisTemplate.boundListOps("OrderLogList").leftPush(logId.toString());
                    } else {
                        //取消本地订单状态,回滚库存，refund:转入退款       closed：已关闭       revoked：已撤销     payerror:支付失败
                        orderService.deleteOrder(orderLog);
                    }
                }
            }
        }
    }
}
