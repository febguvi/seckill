package com.gunjo.skilldemo.controller;

import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.OrderService;
import com.gunjo.skilldemo.vo.OrderDetailVo;
import com.gunjo.skilldemo.vo.RespBean;
import com.gunjo.skilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单详情
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }
}
