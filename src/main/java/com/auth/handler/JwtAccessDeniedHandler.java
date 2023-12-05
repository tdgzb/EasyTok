package com.auth.handler;


import com.alibaba.fastjson2.JSON;
import com.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        log.error(e == null ? "用户没有授权资源" : e.getMessage());
        httpServletResponse.getWriter().write(JSON.toJSONString(new CommonResult(HttpServletResponse.SC_UNAUTHORIZED, e == null ? "用户没有授权资源" : e.getMessage(),false)));
    }
}
