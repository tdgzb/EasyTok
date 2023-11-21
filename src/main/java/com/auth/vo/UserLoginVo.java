package com.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @DATE: 2023/11/6  21:07
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVo {
    private String token;
    private UserVo userVo;
}
