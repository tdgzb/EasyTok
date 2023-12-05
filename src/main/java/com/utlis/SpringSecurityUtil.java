package com.utlis;

import com.auth.dto.JwtUserDto;
import com.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Description:
 * @DATE: 2023/11/29  16:52
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
public class SpringSecurityUtil {
    /**
     * 获得当前已认证的用户
     * @return UserDetails
     */
    public static UserDetails getCurrentUser(){
//        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (null == authentication) {
//                throw new BadRequestException(HttpStatus.UNAUTHORIZED,"当前登录状态过期");
//            }
        System.out.println(authentication.getPrincipal());
            return (JwtUserDto) authentication.getPrincipal();
//        }catch (Exception e){
//            throw new BadRequestException("暂未登录！请重新登录！");
//        }
    }
    public static Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUserDto principal =  (JwtUserDto)authentication.getPrincipal();
        return principal.getUser().getId();
    }
}
