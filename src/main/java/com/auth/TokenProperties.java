package com.auth;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class TokenProperties {

    /**
     * jwt请求头名
     */
    private String header;

    /**
     * 请求头值前缀
     */
    private String prefix;

    /**
     * base64密钥
     */
    private String base64Secret;

    /**
     * 过期时间
     */
    private Long expiration;

    /**
     * 在线用户key缓存键名前缀
     */
    private String onlineKey;

}
