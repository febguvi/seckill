package com.gunjo.skilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gunjo.skilldemo.pojo.SeckillOrder;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.SeckillOrderService;
import com.gunjo.skilldemo.mapper.SeckillOrderMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder>
    implements SeckillOrderService{

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取秒杀结果
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if (seckillOrder != null) {
            return seckillOrder.getOrderId();
        } else if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
            return -1L;
        } else {
            return 0L;
        }

    }
}




