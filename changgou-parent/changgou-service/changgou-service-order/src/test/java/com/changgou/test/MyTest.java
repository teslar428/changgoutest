package com.changgou.test;


import com.changgou.goods.pojo.Sku;
import com.changgou.order.OrderApplication;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import com.changgou.order.service.impl.CartServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class MyTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void fun01() {
        CartService cartService = new CartServiceImpl();
        cartService.add(6, Long.parseLong("1148477873208696832"), "zsitcastt");
    }

    @Test
    public void fun02() {
        List<OrderItem> xxx = redisTemplate.boundHashOps("Cart_szitheima").values();

        //   redisTemplate.delete("Cart_szitheima");
//        Sku sku = new Sku();
//        sku.setName("zs");
//        sku.setStatus("11");
//        sku.setBrandName("啦啦啦啦");
//    //    redisTemplate.boundHashOps("it").put("1", sku);

        //1148477873196113920  1148477873208696832  cart0szitheima

        for (OrderItem orderItem : xxx) {
            System.out.println("...............................spuid:" + orderItem.getSpuId());
            System.out.println("...............................skuid:" + orderItem.getSkuId());
        }
    }

    @Test
    public void fun03() {
        Object cart_szitheima = redisTemplate.boundHashOps("Cart_szitheima").get("1148477873196113920");
        System.out.println(cart_szitheima);
    }

    @Test
    public void fun04() {
        redisTemplate.boundHashOps("Cart_szitheima").delete("1148477873175142400");
    }

    @Test
    public void fun05(){
    }

}
