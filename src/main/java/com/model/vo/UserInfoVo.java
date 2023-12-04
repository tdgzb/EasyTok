package com.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Description:
 * @DATE: 2023/12/4  21:12
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Data
public class UserInfoVo {


    /**
     * 用户名
     */
    private String username;
    /**
     * 头像路径
     */
    private String imgUrl;
    /**
     * 个人简介
     */
    private String description;
    /**
     * 性别
     */
    private Integer  sex;
    /**
     * 大学
     */
    private String university;
}
