package com.config;

import com.auth.TokenProperties;
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
        return new TokenProperties();
    }
}
