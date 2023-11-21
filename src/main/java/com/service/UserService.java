package com.service;

import com.auth.dto.JwtUserDto;
import com.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.UserLoginDto;

/**
* @author 22577
* @description 针对表【user】的数据库操作Service
* @createDate 2023-10-25 19:55:57
*/
public interface UserService extends IService<User> {

    boolean checkNotExisted(String username);

    void register(UserLoginDto authUserDto);
}
