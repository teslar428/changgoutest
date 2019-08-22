package com.changgou.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    //令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";

    private static final String USER_LOGIN_URL = "http://localhost:9001/oauth/login";

    //全局过滤器
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (path.startsWith("/api/user/login")||path.startsWith("/api/user/add") || path.startsWith("/api/brand/search/") || !URLFilter.hasAuthorize(path)) {
            //放行
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }

        //获取头文件中的令牌信息
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        //如果头信息中没有,则从请求参数中获取
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        //从Cookie中获取令牌数据
        HttpCookie first = request.getCookies().getFirst(AUTHORIZE_TOKEN);
        if (first != null) {
            token = first.getValue();
        }

        //如果请求参数中也没有token,则跳转到登录页面
        if (StringUtils.isEmpty(token)) {
            return needAuthorization(USER_LOGIN_URL + "?FROM=" + request.getURI(), exchange);//FROM为原目标URI
        }
        //解析令牌数据
        try {
            request.mutate().header(AUTHORIZE_TOKEN, "Bearer " + token);
        } catch (Exception e) {
            e.printStackTrace();
            return needAuthorization(USER_LOGIN_URL + "?FROM=" + request.getURI(), exchange);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> needAuthorization(String url, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", url);//设置头文件的跳转地址
        return exchange.getResponse().setComplete();
    }
}
