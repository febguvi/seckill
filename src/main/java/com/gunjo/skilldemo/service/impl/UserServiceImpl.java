package com.gunjo.skilldemo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gunjo.skilldemo.exception.GlobalException;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.UserService;
import com.gunjo.skilldemo.mapper.UserMapper;
import com.gunjo.skilldemo.utils.CookieUtil;
import com.gunjo.skilldemo.utils.MD5Utils;
import com.gunjo.skilldemo.utils.UUIDUtil;
import com.gunjo.skilldemo.vo.LoginVo;
import com.gunjo.skilldemo.vo.RespBean;
import com.gunjo.skilldemo.vo.RespBeanEnum;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.*;
import javax.servlet.http.*;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 登录
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 参数判断
//        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
////            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
////        }
////        if (!ValidatorUtil.isMobile(mobile)) {
////            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
////        }

        // 根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 判断密码是否正确
        if (!MD5Utils.fromPassToDBPass(password, user.getSlat()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        // 生成cookie
        String ticket = UUIDUtil.uuid();
        // 将用户信息存入redis中
        redisTemplate.opsForValue().set("user:"+ticket, user);
//        request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return RespBean.success();
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }

        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);

        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }

        return user;
    }
}




