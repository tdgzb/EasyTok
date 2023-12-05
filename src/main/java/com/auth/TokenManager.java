package com.auth;


import com.auth.dto.JwtUserDto;
import com.domain.User;
import com.utlis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Slf4j
@Component
public class TokenManager {

    @Resource
    private TokenProperties tokenProperties;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 根据用户详情生成令牌
     *
     * @param user 用户详情
     * @return 令牌
     */
    public String generateToken(JwtUserDto user) {
        Map<String, Object> claims = new HashMap<>(16);
        //添加主体信息
        claims.put("sub", user.getUsername());
        claims.put("iat", new Date());
        return generateToken(claims);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>(16);
        //添加主体信息
        claims.put("sub", user.getUsername());
        claims.put("iat", new Date());
        return generateToken(claims);
    }

    /**
     * 根据声明生成令牌
     *
     * @param claims 声明
     * @return 令牌
     */
    public String generateToken(Map<String, Object> claims) {
        //生成令牌
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, tokenProperties.getBase64Secret())
                .compact();
    }



    /**
     * 验证令牌是否过期
     *
     * @param token 令牌
     * @return T/F
     */
    public boolean isExpired(String token) {
        long expire = redisUtil.getExpire(tokenProperties.getOnlineKey() + getUsernameFromToken(token));
        return expire < 0;
    }

    /**
     * 从令牌中获得用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public Long getUserId(String token){
        Claims claims = getClaimsFromToken(token);
        return Long.valueOf(claims.getId());
    }

    /**
     * 从令牌中获得声明
     *
     * @param token 令牌
     * @return 声明
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims=null;
        try {
            //转译令牌
            claims = Jwts.parser()
                    .setSigningKey(tokenProperties.getBase64Secret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            //抛出非法凭证异常
            log.error("非法凭证");
        }
        return claims;
    }

}
