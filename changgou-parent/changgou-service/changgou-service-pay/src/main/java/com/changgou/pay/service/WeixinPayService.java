package com.changgou.pay.service;

import java.util.Map;

public interface WeixinPayService {
    Map createNative(Map<String,String> parameters);

    Map queryPayStatus(String out_trade_no);

    Map<String, String> closePay(Long orderId) throws Exception;
}
