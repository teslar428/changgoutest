package com.changgou.seckill.task;

import com.changgou.entity.DateUtil;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class SeckillGoodsPushTask {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis() {
        //获取时间段集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        //循环时间段
        for (Date startTime : dateMenus) {
            String extName = DateUtil.data2str(startTime, DateUtil.PATTERN_YYYYMMDDHH);
            //根据时间段查询对应的商品数据
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", "1");//审核通过
            criteria.andGreaterThan("stockCount", 0);//库存>0
            criteria.andGreaterThanOrEqualTo("startTime", startTime);//开始时间<=活动开始时间
            criteria.andLessThan("endTime", DateUtil.addDateHour(startTime, 2));//活动结束时间<(开始时间+2小时)
            //排除之前已经加载到redis中的商品数据
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + extName).keys();
            if (keys != null && keys.size() > 0) {
                criteria.andNotIn("id", keys);
            }
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps("SeckillGoods_" + extName).put(seckillGood.getId(), seckillGood);

                //商品数据队列储存,防止高并发超卖
                Long[] ids = pushIds(seckillGood.getStockCount(), seckillGood.getId());
                redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillGood.getId()).leftPushAll(ids);
                //自增计数器
                redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillGood.getId(), seckillGood.getStockCount());
            }
        }
    }

    //将商品ID存入到数组中
    public Long[] pushIds(int len, Long id) {
        Long[] ids = new Long[len];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = id;
        }
        return ids;
    }
}
