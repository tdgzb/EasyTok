package com.config;


import com.Interceptor.LoginInterceptor;
import com.utlis.RedisUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Resource
    private RedisUtil redisUtil;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor(redisUtil))
                .excludePathPatterns(
                        "/user/login",
                        "/user/register"
                );
    }
}
