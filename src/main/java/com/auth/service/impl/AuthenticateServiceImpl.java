package com.auth.service.impl;


import com.auth.TokenManager;
import com.auth.TokenProperties;
import com.auth.dto.JwtUserDto;
import com.auth.service.AuthenticateService;
import com.exception.BadRequestException;
import com.utlis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 认证业务类
 *
 * @author 黄雨恒 <799620154@qq.com>
 */

@Service
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private TokenManager tokenManager;

    @Resource
    private TokenProperties tokenProperties;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Authentication authenticate(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            //认证
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return authenticate;
        } catch (UsernameNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        } catch (DisabledException e){
            throw new BadRequestException("用户被禁用");
        } catch (BadCredentialsException e){
            throw new BadRequestException("用户名密码错误");
        }
    }

    @Override
    public String generateToken(Authentication authentication) {
        return tokenManager.generateToken((JwtUserDto) authentication.getPrincipal());
    }

    @Override
    public Long getUserId(Authentication authentication) {
        return ((JwtUserDto) authentication.getPrincipal()).getUser().getId();
    }

    @Override
    public String getUserName(Authentication authentication) {
        return ((JwtUserDto) authentication.getPrincipal()).getUser().getUsername();
    }

    @Override
    public List<Object> getRole(Authentication authentication) {
        return ((JwtUserDto) authentication.getPrincipal()).getRole();
    }

    @Override
    public void saveToken(String token, String username) {
        redisUtil.set(tokenProperties.getOnlineKey() + username, token, tokenProperties.getExpiration() / 1000);
    }

    @Override
    public void singleLogin(String newToken, String username) {
        if (StringUtils.isNotBlank(username)) {
            try {
                saveToken(newToken, username);
            } catch (Exception e) {
                log.error("单例登录异常", e);
            }
        }
    }

    @Override
    public void logout(String username) {
        String key = tokenProperties.getOnlineKey() + username;
        redisUtil.delete(key);
    }

}
