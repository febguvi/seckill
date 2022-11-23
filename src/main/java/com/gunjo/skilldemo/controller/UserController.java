package com.gunjo.skilldemo.controller;

import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.rabbitmq.MQSender;
import com.gunjo.skilldemo.vo.RespBean;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;

    /**
     * 用户信息
     */
    @ResponseBody
    @RequestMapping("/info")
    public RespBean info(User user) {
        return RespBean.success(user);
    }

    /**
     * 测试RabbitMQ发送消息

    @RequestMapping("/mq")
    @ResponseBody
    public void mq() {
        mqSender.send("hello");
    }
     */


}
