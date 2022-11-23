package com.gunjo.skilldemo.service;

import com.gunjo.skilldemo.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gunjo.skilldemo.vo.LoginVo;
import com.gunjo.skilldemo.vo.RespBean;

import javax.servlet.http.*;

/**
 *
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    // 根据cookie获取用户
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    // 修改密码
    RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);


}
