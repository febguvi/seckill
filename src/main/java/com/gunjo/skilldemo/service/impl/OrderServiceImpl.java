package com.gunjo.skilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gunjo.skilldemo.exception.GlobalException;
import com.gunjo.skilldemo.pojo.Order;
import com.gunjo.skilldemo.pojo.SeckillGoods;
import com.gunjo.skilldemo.pojo.SeckillOrder;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.GoodsService;
import com.gunjo.skilldemo.service.OrderService;
import com.gunjo.skilldemo.mapper.OrderMapper;
import com.gunjo.skilldemo.service.SeckillGoodsService;
import com.gunjo.skilldemo.service.SeckillOrderService;
import com.gunjo.skilldemo.utils.MD5Utils;
import com.gunjo.skilldemo.utils.UUIDUtil;
import com.gunjo.skilldemo.vo.GoodsVo;
import com.gunjo.skilldemo.vo.OrderDetailVo;
import com.gunjo.skilldemo.vo.RespBeanEnum;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import javax.annotation.*;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Resource
    private OrderMapper orderMapper;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Order secKill(User user, GoodsVo goodsVo) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count = stock_count - 1").eq("id", seckillGoods.getId()).gt("stock_count", 0));
        if (seckillGoods.getStockCount() < 1) {
            // 判断是否还有库存
            valueOperations.set("isStockEmpty:" + goodsVo.getId(), "0");
            return null;   // 如果没更新成功
        }
        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goodsVo.getId(), seckillOrder);
        return order;
    }

    /**
     * 订单详情
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);
        return detail;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Utils.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + goodsId, str, 60, TimeUnit.SECONDS);
        return str;

    }

    /**
     * 校验秒杀地址
     */
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /**
     * 校验验证码的输入结果
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}




