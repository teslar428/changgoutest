package com.changgou.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.HttpClient;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;

    @Override
    public Map createNative(Map<String, String> parameters) {
        try {
            String outtradeno = parameters.get("outtradeno");
            String money = parameters.get("totalfee");
            Map param = new HashMap();

            //1.封装参数
            param.put("appid", appid);//应用ID
            param.put("mch_id", partner);//商户ID
            param.put("nonce_str", WXPayUtil.generateNonceStr());//随机数
            param.put("body", "畅购");//订单描述
            param.put("out_trade_no", outtradeno);//订单号
            param.put("total_fee", money);//交易金额
            param.put("spbill_create_ip", "127.0.0.1");//终端IP
            param.put("notify_url", notifyurl);//回调地址
            param.put("trade_type", "NATIVE");//交易类型

//            String exchange = parameters.get("exchange");
//            String routingkey = parameters.get("routingkey");
//            String username = parameters.get("username");
//            Map<String, String> attachMap = new HashMap<String, String>();
//            attachMap.put("exchange", exchange);
//            attachMap.put("routingkey", routingkey);
//            attachMap.put("username", username);
//            param.put("attach", JSON.toJSONString(attachMap));
            param.put("attach", JSON.toJSONString(parameters));

            //2.将参数转成xml字符,并携带签名
            String paraXml = WXPayUtil.generateSignedXml(param, partnerkey);

            //3.执行请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paraXml);
            httpClient.post();

            //4.获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("stringMap:" + stringMap);

            return stringMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //查询订单状态
    @Override
    public Map queryPayStatus(String out_trade_no) {
        try {
            Map param = new HashMap();
            param.put("appid", appid);//应用ID
            param.put("mch_id", partner);//商户ID
            param.put("out_trade_no", out_trade_no);//订单号
            param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符

            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);

            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> closePay(Long orderId) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appid", appid);
        paramMap.put("mch_id", partner);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("out_trade_no", String.valueOf(orderId));

        String xmlParam = WXPayUtil.generateSignedXml(paramMap, partnerkey);
        String url = "https://api.mch.weixin.qq.com/pay/closeorder";

        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);
        httpClient.setXmlParam(xmlParam);
        httpClient.post();

        String content = httpClient.getContent();
        return WXPayUtil.xmlToMap(content);
    }
}
