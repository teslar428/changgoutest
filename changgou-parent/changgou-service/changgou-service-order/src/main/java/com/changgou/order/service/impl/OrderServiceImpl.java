package com.changgou.order.service.impl;

import com.changgou.entity.IdWorker;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderLogMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.pojo.OrderLog;
import com.changgou.order.service.CartService;
import com.changgou.order.service.OrderService;
import com.changgou.user.feign.UserFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private OrderLogMapper orderLogMapper;

    // Order条件+分页查询
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(order);
        //执行搜索
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    // Order分页查询
    @Override
    public PageInfo<Order> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    // Order条件查询
    @Override
    public List<Order> findList(Order order) {
        //构建查询条件
        Example example = createExample(order);
        //根据构建的条件查询数据
        return orderMapper.selectByExample(example);
    }

    // Order构建查询对象
    public Example createExample(Order order) {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if (order != null) {
            // 订单id
            if (!StringUtils.isEmpty(order.getId())) {
                criteria.andEqualTo("id", order.getId());
            }
            // 数量合计
            if (!StringUtils.isEmpty(order.getTotalNum())) {
                criteria.andEqualTo("totalNum", order.getTotalNum());
            }
            // 金额合计
            if (!StringUtils.isEmpty(order.getTotalMoney())) {
                criteria.andEqualTo("totalMoney", order.getTotalMoney());
            }
            // 优惠金额
            if (!StringUtils.isEmpty(order.getPreMoney())) {
                criteria.andEqualTo("preMoney", order.getPreMoney());
            }
            // 邮费
            if (!StringUtils.isEmpty(order.getPostFee())) {
                criteria.andEqualTo("postFee", order.getPostFee());
            }
            // 实付金额
            if (!StringUtils.isEmpty(order.getPayMoney())) {
                criteria.andEqualTo("payMoney", order.getPayMoney());
            }
            // 支付类型，1、在线支付、0 货到付款
            if (!StringUtils.isEmpty(order.getPayType())) {
                criteria.andEqualTo("payType", order.getPayType());
            }
            // 订单创建时间
            if (!StringUtils.isEmpty(order.getCreateTime())) {
                criteria.andEqualTo("createTime", order.getCreateTime());
            }
            // 订单更新时间
            if (!StringUtils.isEmpty(order.getUpdateTime())) {
                criteria.andEqualTo("updateTime", order.getUpdateTime());
            }
            // 付款时间
            if (!StringUtils.isEmpty(order.getPayTime())) {
                criteria.andEqualTo("payTime", order.getPayTime());
            }
            // 发货时间
            if (!StringUtils.isEmpty(order.getConsignTime())) {
                criteria.andEqualTo("consignTime", order.getConsignTime());
            }
            // 交易完成时间
            if (!StringUtils.isEmpty(order.getEndTime())) {
                criteria.andEqualTo("endTime", order.getEndTime());
            }
            // 交易关闭时间
            if (!StringUtils.isEmpty(order.getCloseTime())) {
                criteria.andEqualTo("closeTime", order.getCloseTime());
            }
            // 物流名称
            if (!StringUtils.isEmpty(order.getShippingName())) {
                criteria.andEqualTo("shippingName", order.getShippingName());
            }
            // 物流单号
            if (!StringUtils.isEmpty(order.getShippingCode())) {
                criteria.andEqualTo("shippingCode", order.getShippingCode());
            }
            // 用户名称
            if (!StringUtils.isEmpty(order.getUsername())) {
                criteria.andLike("username", "%" + order.getUsername() + "%");
            }
            // 买家留言
            if (!StringUtils.isEmpty(order.getBuyerMessage())) {
                criteria.andEqualTo("buyerMessage", order.getBuyerMessage());
            }
            // 是否评价
            if (!StringUtils.isEmpty(order.getBuyerRate())) {
                criteria.andEqualTo("buyerRate", order.getBuyerRate());
            }
            // 收货人
            if (!StringUtils.isEmpty(order.getReceiverContact())) {
                criteria.andEqualTo("receiverContact", order.getReceiverContact());
            }
            // 收货人手机
            if (!StringUtils.isEmpty(order.getReceiverMobile())) {
                criteria.andEqualTo("receiverMobile", order.getReceiverMobile());
            }
            // 收货人地址
            if (!StringUtils.isEmpty(order.getReceiverAddress())) {
                criteria.andEqualTo("receiverAddress", order.getReceiverAddress());
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if (!StringUtils.isEmpty(order.getSourceType())) {
                criteria.andEqualTo("sourceType", order.getSourceType());
            }
            // 交易流水号
            if (!StringUtils.isEmpty(order.getTransactionId())) {
                criteria.andEqualTo("transactionId", order.getTransactionId());
            }
            // 订单状态,0:未完成,1:已完成，2：已退货
            if (!StringUtils.isEmpty(order.getOrderStatus())) {
                criteria.andEqualTo("orderStatus", order.getOrderStatus());
            }
            // 支付状态,0:未支付，1：已支付，2：支付失败
            if (!StringUtils.isEmpty(order.getPayStatus())) {
                criteria.andEqualTo("payStatus", order.getPayStatus());
            }
            // 发货状态,0:未发货，1：已发货，2：已收货
            if (!StringUtils.isEmpty(order.getConsignStatus())) {
                criteria.andEqualTo("consignStatus", order.getConsignStatus());
            }
            // 是否删除
            if (!StringUtils.isEmpty(order.getIsDelete())) {
                criteria.andEqualTo("isDelete", order.getIsDelete());
            }
        }
        return example;
    }

    // 删除
    @Override
    public void delete(String id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    // 修改Order
    @Override
    public void update(Order order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

    // 增加Order
    @Override
    public int add(Order order) {
        //查询用户的所有购物车数据
        List<OrderItem> orderItemList = cartService.list(order.getUsername());
        //统计计算
        int totalMoney = 0;
        int totalPayMoney = 0;
        int num = 0;
        for (OrderItem orderItem : orderItemList) {
            //总金额
            totalMoney += orderItem.getMoney();
            //实际支付金额
            totalPayMoney += orderItem.getPayMoney();
            //总数量
            num += orderItem.getNum();
        }
        order.setTotalNum(num);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalPayMoney);
        order.setPreMoney(totalMoney - totalPayMoney);

        //其他数据完善
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");//0:未评价,1:已评价
        order.setSourceType("1");//1:web订单
        order.setOrderStatus("0");//0:未完成,1:已完成
        order.setPayStatus("0");//0:未支付,1:已支付,2:支付失败
        order.setConsignStatus("0");//0:未发货,1:已发货,2:已收货
        order.setId("NO." + idWorker.nextId());
        int count = orderMapper.insertSelective(order);

        //增加订单明细
        for (OrderItem orderItem : orderItemList) {
            orderItem.setId("NO." + idWorker.nextId());
            orderItem.setIsReturn("0");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);
        }

        //线上支付,记录支付日志
        if (order.getPayType().equals("1")) {
            OrderLog orderLog = new OrderLog();
            orderLog.setId(String.valueOf(idWorker.nextId()));
            orderLog.setOrderId(order.getId());
            orderLog.setMoney(order.getPayMoney());
            orderLog.setOrderStatus(order.getOrderStatus());
            orderLog.setPayStatus(order.getPayStatus());
            orderLog.setConsignStatus(order.getConsignStatus());
            orderLog.setUsername(order.getUsername());
            orderLog.setRemarks("创建支付记录");

            //存储完整日志信息
            redisTemplate.boundHashOps("OrderLog").put(orderLog.getId(), orderLog);
            //拥有定时任务定时读取,检测是否已支付
            redisTemplate.boundListOps("OrderLogList").leftPush(orderLog.getId());
            //存放用户对应的订单和订单日志id
            redisTemplate.boundHashOps("OrderMappingLog" + order.getUsername()).put(order.getId(), orderLog.getId());
        }

        //库存减少
        skuFeign.decrCount(order.getUsername());

//        redisTemplate.delete("Cart_" + order.getUsername());
        return count;
    }

    // 根据ID查询Order
    @Override
    public Order findById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    // 查询Order全部数据
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /***
     * 修改订单状态
     * @param username：用户登录名
     * @param orderId：订单ID
     * @param transactionId：交易流水号
     * @param orderLog:日志数据
     */
    @Override
    public void updateStatus(String username, String orderId, String transactionId, OrderLog orderLog) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null) {
            order.setTransactionId(transactionId);
            order.setPayStatus("1");//支付状态,0:未支付,1:已支付,2:支付失败
            order.setPayTime(new Date());//支付时间
            order.setUpdateTime(new Date());//更新时间

            //修改日志支付状态
            orderLog.setPayStatus(order.getPayStatus());

            //更新数据到数据库
            orderMapper.updateByPrimaryKeySelective(order);
            orderLogMapper.insertSelective(orderLog);

            //增加积分
            userFeign.addPoints(10);

            redisTemplate.delete("Cart_" + order.getUsername());

            //清理redis缓存
            redisTemplate.boundHashOps("OrderMappingLog" + order.getUsername()).delete(order.getId());
            redisTemplate.boundHashOps("OrderLog").delete(orderLog.getId());
        }
    }

    @Override
    public void deleteOrder(OrderLog orderLog) {
        Order order = orderMapper.selectByPrimaryKey(orderLog.getOrderId());
        order.setPayStatus("2");
        order.setUpdateTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);

        skuFeign.incrCount(order.getUsername());

        redisTemplate.boundHashOps("OrderMappingLog" + order.getUsername()).delete(order.getId());
        redisTemplate.boundHashOps("OrderLog").delete(orderLog.getId());
    }
}
