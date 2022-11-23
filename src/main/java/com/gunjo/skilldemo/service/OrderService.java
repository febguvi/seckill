package com.gunjo.skilldemo.service;

import com.gunjo.skilldemo.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.vo.GoodsVo;
import com.gunjo.skilldemo.vo.OrderDetailVo;

/**
 *
 */
public interface OrderService extends IService<Order> {

    Order secKill(User user, GoodsVo goodsVo);

    // 订单详情
    OrderDetailVo detail(Long orderId);

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, Long goodsId, String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
