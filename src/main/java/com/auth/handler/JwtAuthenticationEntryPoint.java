package com.auth.handler;


import com.alibaba.fastjson2.JSON;
import com.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description
 *
 * @author 黄雨恒 <799620154@qq.com>
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error(e == null ? "用户访问资源没有携带正确的令牌" : e.getMessage());
        httpServletResponse.getWriter().write(JSON.toJSONString(new CommonResult(HttpServletResponse.SC_UNAUTHORIZED, e == null ? "用户访问资源没有携带正确的token" : e.getMessage(),false)));
    }
}
