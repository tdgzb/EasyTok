package com.service.impl;

import com.auth.dto.JwtUserDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.Role;
import com.domain.User;
import com.dto.UserLoginDto;
import com.mapper.RoleMapper;
import com.service.UserService;
import com.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 22577
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-10-25 19:55:57
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    RoleMapper roleMapper;

    @Override
    public void register(UserLoginDto authUserDto) {
        User user = new User();
        BeanUtils.copyProperties(authUserDto, user);
        System.out.println(user);
//        user.setUsername(authUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, "user");
        Role role = roleMapper.selectOne(wrapper);
        roleMapper.insertRole(user.getId(), role.getId());
    }

    @Override
    public boolean checkNotExisted(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        return user == null;
    }
}




