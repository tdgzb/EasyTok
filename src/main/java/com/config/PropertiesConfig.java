package com.config;

import com.auth.TokenProperties;
import com.oss.AliOSSProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class PropertiesConfig {

    /**
     * tokenProperties实体类注入匹配值
     * @return tokenProperties
     */
    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public TokenProperties jwtProperties(){
        System.out.println(new TokenProperties());
        return new TokenProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "oss")
    public AliOSSProperties aliOSSProperties(){
        System.out.println(new AliOSSProperties());
        return new AliOSSProperties();
    }
}
