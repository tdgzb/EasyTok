package com.auth.service;

import com.auth.dto.JwtUserDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.constants.RedisConstants;
import com.domain.Menu;
import com.domain.Role;
import com.domain.User;
import com.exception.BusinessException;
import com.mapper.MenuMapper;
import com.mapper.RoleMapper;
import com.mapper.UserMapper;
import com.result.ResultConstant;
import com.utlis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @DATE: 2023/11/6  20:12
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ResultConstant.USER_NULL, "用户信息不存在");
        }
        List<Object> roles = redisUtil.lGet(RedisConstants.ROLE + username);
        if (roles==null||roles.size()==0){
           roles = roleMapper.selectRoleByUid(user.getId());
           redisUtil.lSet(RedisConstants.ROLE + username, roles);
        }
        redisUtil.delete(RedisConstants.MENU + username);
        List<Object> menus=redisUtil.lGet(RedisConstants.MENU + username, 0, -1);
        if (menus == null || menus.size() == 0) {
            menus = menuMapper.selectMenuByUid(user.getId());
            redisUtil.lSet(RedisConstants.MENU + username, menus);
        }
        List<GrantedAuthority> authorities =  AuthorityUtils.commaSeparatedStringToAuthorityList(getAuth(menus));
        return new JwtUserDto(user, user.getUsername(), passwordEncoder.encode(user.getPassword()), roles, authorities);
    }

    public static String getAuth(List<Object> permission) {
        String list = permission.toString();
        return list.substring(1, list.length() - 1);
    }
}