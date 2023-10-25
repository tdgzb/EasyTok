package com.Interceptor;


import com.utlis.RedisUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @DATE: 2023/10/25  15:33
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */

public class LoginInterceptor implements HandlerInterceptor {
    
    private RedisUtil redisUtil;

    public LoginInterceptor(RedisUtil redisUtil) {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
