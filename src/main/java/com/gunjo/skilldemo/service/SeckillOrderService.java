package com.gunjo.skilldemo.service;

import com.gunjo.skilldemo.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gunjo.skilldemo.pojo.User;

/**
 *
 */
public interface SeckillOrderService extends IService<SeckillOrder> {

    /**
     * 获取秒杀结果
     */
    Long getResult(User user, Long goodsId);
}
