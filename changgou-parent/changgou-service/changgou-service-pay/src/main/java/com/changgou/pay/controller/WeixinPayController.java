package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weixin/pay")
@CrossOrigin
public class WeixinPayController {
    @Autowired
    private WeixinPayService weixinPayService;

    @Value("${mq.pay.exchange.order}")
    private String exchange;

    @Value("${mq.pay.queue.order}")
    private String queue;

    @Value("${mq.pay.routing.orderkey}")
    private String routing;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/create/native")
    public Result createNative(@RequestParam Map<String, String> parameter) {
        Map<String, String> resultMap = weixinPayService.createNative(parameter);
        return new Result(true, StatusCode.OK, "创建二维码预付订单成功", resultMap);
    }

    @GetMapping("/status/query")
    public Result queryStatus(String outtradeno) {
        Map<String, String> resultMap = weixinPayService.queryPayStatus(outtradeno);
        return new Result(true, StatusCode.OK, "查询订单状态成功", resultMap);
    }

    @RequestMapping("/notify/url")
    public String notifyUrl(HttpServletRequest request) {
        InputStream inputStream;
        try {
            inputStream = request.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.close();
            inputStream.close();

            String result = new String(outputStream.toByteArray());

            //将支付回调数据转换成xml字符串
            Map<String, String> map = WXPayUtil.xmlToMap(result);

            Map<String, String> attach = JSON.parseObject(map.get("attach"), Map.class);
            //发送消息
            rabbitTemplate.convertAndSend(exchange, attach.get("queue"), JSON.toJSONString(map));

            Map resultMap = new HashMap();
            resultMap.put("return_code", "SUCCESS");
            resultMap.put("return_msg", "OK");
            return WXPayUtil.mapToXml(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/close")
    public Result closePay(Long orderId) throws Exception {
        Map<String, String> map = weixinPayService.closePay(orderId);
        return new Result(true, StatusCode.OK, "关闭订单成功", map);
    }
}
