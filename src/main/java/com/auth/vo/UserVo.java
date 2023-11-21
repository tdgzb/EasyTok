package com.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @DATE: 2023/11/6  21:08
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private Long id;
    private String username;
    private List<Object> roles;
}
