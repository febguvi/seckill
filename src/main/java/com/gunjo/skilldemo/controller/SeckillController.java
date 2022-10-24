package com.gunjo.skilldemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gunjo.skilldemo.pojo.Order;
import com.gunjo.skilldemo.pojo.SeckillOrder;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.GoodsService;
import com.gunjo.skilldemo.service.OrderService;
import com.gunjo.skilldemo.service.SeckillOrderService;
import com.gunjo.skilldemo.vo.GoodsVo;
import com.gunjo.skilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

/**
 * 秒杀
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private OrderService orderService;

    /**
     *秒杀
     */
    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user, Long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        // 判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
        }
        Order order = orderService.secKill(user, goodsVo);
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";
    }

}
