package com.gunjo.skilldemo.service;

import com.gunjo.skilldemo.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.vo.GoodsVo;

/**
 *
 */
public interface OrderService extends IService<Order> {

    Order secKill(User user, GoodsVo goodsVo);
}
