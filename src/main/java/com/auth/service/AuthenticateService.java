package com.auth.service;

import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * @Description:
 * @DATE: 2023/11/6  20:57
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
public interface AuthenticateService {
    /**
     * 登录认证
     * @param username 用户名
     * @param password 用户密码
     * @return 根据用户信息生成的认证
     */
    Authentication authenticate(String username, String password);

    /**
     * 根据认证生成token
     * @param authentication 认证信息
     * @return token
     */
    String generateToken(Authentication authentication);

    /**
     * 根据认证获得用户id
     * @param authentication 认证信息
     * @return 用户ID
     */
    Long getUserId(Authentication authentication);

    String getUserName(Authentication authentication);

    /**
     * 根据认证获得用户role
     * @param authentication 认证信息
     * @return 用户角色
     */
    List<Object> getRole(Authentication authentication);

    /**
     * redis添加token并设置过期时间
     * @param token 令牌
     * @param username 用户名
     */
    void saveToken(String token,String username);

    /**
     * 检测用户是否登录, 已经登录挤掉已经登录的用户
     * @param username 用户名
     * @param newToken 新的令牌
     */
    void singleLogin(String username,String newToken);

    /**
     * 退出登录
     * @param username 当前用户名
     */
    void logout(String username);
}
