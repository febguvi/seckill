package com.gunjo.skilldemo.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.GoodsService;
import com.gunjo.skilldemo.service.UserService;
import com.gunjo.skilldemo.vo.DetailVo;
import com.gunjo.skilldemo.vo.GoodsVo;
import com.gunjo.skilldemo.vo.RespBean;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.*;
import org.thymeleaf.spring5.view.*;

import javax.servlet.http.*;
import java.util.*;
import java.util.concurrent.*;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 跳转到商品列表
     */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList (Model model, User user,
                          HttpServletRequest request, HttpServletResponse response) {
//        if (StringUtils.isEmpty(ticket)) {
//            return "login";
//        }
//        User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
        if (user == null) {
            return "login";
        }

        // Redis中获取页面，如果不为空，则直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
//        return "goodsList";

        // 如果为空，手动渲染，存入Redis并返回
        WebContext context = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    /**
     * 跳转商品详情页
     */
    @RequestMapping(value = "/toDetail2/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2 (Model model, User user, @PathVariable Long goodsId,
                            HttpServletRequest request, HttpServletResponse response) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // Redis中获取页面，如果不为空，直接返回页面
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
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
//        return "goodsDetail";

        // 如果为空，手动渲染
        WebContext context = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsDetail", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }



    /**
     * 跳转商品详情页
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail (User user, @PathVariable Long goodsId) {
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

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }
}
