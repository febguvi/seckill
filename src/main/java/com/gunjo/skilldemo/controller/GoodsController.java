package com.gunjo.skilldemo.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.GoodsService;
import com.gunjo.skilldemo.service.UserService;
import com.gunjo.skilldemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.*;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 跳转到商品列表
     */
    @RequestMapping("/toList")
    public String toList (Model model, User user) {
//        if (StringUtils.isEmpty(ticket)) {
//            return "login";
//        }
//        User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }

    /**
     * 跳转商品详情页
     */
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail (Model model, User user, @PathVariable Long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        // 秒杀状态
        int secKillStatus = 0;
        // 秒杀倒计时
        int remainSeconds = 0;
        // 秒杀还没开始
        if (nowDate.before(startDate)) {
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)) {
            // 秒杀已经结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("goods", goodsVo);
        return "goodsDetail";
    }
}
