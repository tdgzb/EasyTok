package com.auth.filter;


import com.auth.TokenManager;
import com.auth.TokenProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Slf4j
@Data
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{


    private TokenManager tokenManager ;

    private TokenProperties tokenProperties;

    private UserDetailsService userDetailsService;

    /**
     * 验证token合法和鉴权
     * @param httpServletRequest servletRequest
     * @param httpServletResponse servletResponse
     * @param filterChain filterChain
     * @throws IOException IOException
     * @throws ServletException ServletException
     */

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获得token
        String token = httpServletRequest.getHeader(tokenProperties.getHeader());
        if (StringUtils.isNotBlank(token)) {
            String username = tokenManager.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //认证
                //添加成功合法后的认证信息到全局中
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        System.out.println(token);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
