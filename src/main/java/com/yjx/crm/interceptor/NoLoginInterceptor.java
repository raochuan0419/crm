package com.yjx.crm.interceptor;

import com.yjx.crm.exceptions.NoLoginException;
import com.yjx.crm.service.UserService;
import com.yjx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    非法拦截
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer uid = LoginUserUtil.releaseUserIdFromCookie(request);
        if (uid==null||userService.selectByPrimaryKey(uid)==null) {
            throw new NoLoginException();
        }
        return true;
    }
}
