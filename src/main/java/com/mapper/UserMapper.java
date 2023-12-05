package com.mapper;

import com.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 22577
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-10-25 19:55:57
* @Entity com.domain.User
*/
public interface UserMapper extends BaseMapper<User> {
   String selectUserName(Long id);
}




