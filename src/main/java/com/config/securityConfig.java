package com.config;
import com.auth.TokenManager;
import com.auth.TokenProperties;
import com.auth.filter.JwtAuthenticationFilter;
import com.auth.handler.JwtAccessDeniedHandler;
import com.auth.handler.JwtAuthenticationEntryPoint;
import com.auth.service.UserDetailServiceImpl;
import com.utlis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @Description:
 * @DATE: 2023/5/22  21:56
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class securityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private TokenManager tokenManager ;
    @Resource
    private TokenProperties tokenProperties;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http .logout().disable()
       .authorizeRequests()
// 放行所有OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS).permitAll()
// 放行登录方法
//                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
//                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
//                .antMatchers(HttpMethod.GET,"/video/random").permitAll()
                .antMatchers(HttpMethod.POST,"/upload/upload").permitAll()
// 其他请求都需要认证后才能访问
                .anyRequest().permitAll()
// 使用自定义的 accessDecisionManager
                .and()
                .csrf().disable()
                .addFilterBefore(new JwtAuthenticationFilter(tokenManager,tokenProperties,userDetailsService),UsernamePasswordAuthenticationFilter.class)
// 添加未登录与权限不足异常处理器
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint()).and()
// 将自定义的JWT过滤器放到过滤链中
// 打开Spring Security的跨域
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
// 关闭CSRF
// 关闭Session机制
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new JwtAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new JwtAuthenticationEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) {
        //静态资源不加载进过滤器链
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


}
