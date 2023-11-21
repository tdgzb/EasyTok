package com.controller;
import com.auth.TokenManager;
import com.auth.dto.JwtUserDto;
import com.auth.service.AuthenticateService;
import com.auth.vo.UserLoginVo;
import com.auth.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.domain.User;
import com.dto.UserLoginDto;
import com.exception.BadRequestException;
import com.result.CommonResult;
import com.service.UserService;
import com.utlis.Sha1Cipher;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @DATE: 2023/10/25  15:38
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @Resource
    TokenManager tokenManager;
    @Resource
    private UserService userService;
    @Resource
    private AuthenticateService authenticateService;


    /**
     * 注册
     * @param authUserDto 前端数据对象
     * @return 统一结果返回
     */
    @PostMapping("/register")
    public CommonResult register(@Validated @RequestBody UserLoginDto authUserDto) {
        System.out.println(authUserDto);
        if (userService.checkNotExisted(authUserDto.getUsername())) {
            userService.register(authUserDto);
            return CommonResult.operateSuccess();
        } else {
            throw new BadRequestException("注册失败,用户名已存在");
        }
    }

            /**
             * 登录认证接口
             *
             * @param authUserDto 登录信息
             * @return 统一返回结果
             */
    @PostMapping("/login")
    public CommonResult authenticate(@Validated @RequestBody UserLoginDto authUserDto) {
        //认证
        Authentication authenticate = authenticateService.authenticate(authUserDto.getUsername(), authUserDto.getPassword());
        //认证后生成令牌
        String token = authenticateService.generateToken(authenticate);
        Long uid = authenticateService.getUserId(authenticate);
        String username=authenticateService.getUserName(authenticate);
        authenticateService.singleLogin(token, authUserDto.getUsername());
        List<Object> roles = authenticateService.getRole(authenticate);
        UserLoginVo userLoginVo=new UserLoginVo();
        userLoginVo.setToken(token);
        userLoginVo.setUserVo(new UserVo(uid,username,roles));
         return CommonResult.operateSuccess(userLoginVo);
    }
}
