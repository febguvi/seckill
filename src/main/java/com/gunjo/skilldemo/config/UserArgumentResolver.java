package com.gunjo.skilldemo.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gunjo.skilldemo.pojo.User;
import com.gunjo.skilldemo.service.UserService;
import com.gunjo.skilldemo.utils.CookieUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.support.*;
import org.springframework.web.context.request.*;
import org.springframework.web.method.support.*;

import javax.servlet.http.*;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (StringUtils.isEmpty(ticket)) {
            return null;
        }
        return userService.getUserByCookie(ticket, request, response);
    }
}
