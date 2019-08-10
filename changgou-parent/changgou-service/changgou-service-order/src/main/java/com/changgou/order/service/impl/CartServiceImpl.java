package com.changgou.order.service.impl;

import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    /***
     * 加入购物车
     * @param num:购买商品数量
     * @param id：购买ID
     * @param username：购买用户
     * @return
     */
    @Override
    public void add(Integer num, Long id, String username) {
        if (num <= 0) {
            redisTemplate.boundHashOps("Cart_" + username).delete("" + id);
            return;
        }
        Result<Sku> skuResult = skuFeign.findById(id);
        if (skuResult != null && skuResult.isFlag()) {
            //获取sku
            Sku sku = skuResult.getData();
            //获取spu
            Spu spu = spuFeign.findById(sku.getSpuId()).getData();
            //将sku和spu转换成orderitem
            OrderItem orderItem = sku2OrderItem(sku, spu, num);
            //购物车数据存入redis
            redisTemplate.boundHashOps("Cart_" + username).put("" + id, orderItem);
        }
    }

    //查询用户购物车数据
    @Override
    public List<OrderItem> list(String username) {
        List<OrderItem> oderItems = redisTemplate.boundHashOps("Cart_" + username).values();
        return oderItems;
    }

    //Sku转成OrderItem
    private OrderItem sku2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num * orderItem.getPrice());//数量*单价
        orderItem.setPayMoney(num * orderItem.getPrice());//实付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(num * sku.getWeight());//数量*单个重量

        //分类ID设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
