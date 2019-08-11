package com.changgou.filter;

public class URLFilter {

    //购物车订单微服务都需要用户登录，必须携带令牌，所以所有路径都过滤,订单微服务需要过滤的地址
    public static String orderFilterPath = "/api/worder/**,/api/wcart/**,/api/cart/**,/api/categoryReport/**,/api/orderConfig/**,/api/order/**,/api/orderItem/**,/api/orderLog/**,/api/preferential/**,/api/returnCause/**,/api/returnOrder/**,/api/returnOrderItem/**";
    public static String userFilterPath = "/api/user/**,/api/address/**";

    /***
     * 检查请求路径是否需要进行权限校验
     * @return true:需要权限校验   false:无需权限校验
     */
    public static boolean hasAuthorize(String uri) {
        //替换掉所有**,并切割
        String[] urls = orderFilterPath.replace("**", "").split(",");
        for (String url : urls) {
            //判断当前地址是否需要用户权限
            if (uri.startsWith(url)) {
                return true;
            }
        }

        String[] userUrls = userFilterPath.replace("**", "").split(",");
        for (String url : userUrls) {
            //判断当前地址是否需要用户权限
            if (uri.startsWith(url)) {
                return true;
            }
        }
        return false;
    }
}
