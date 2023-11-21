package com.auth.dto;

import com.domain.Role;
import com.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * 自定义userDetails用户信息dto
 *
 * @author 黄雨恒 <799620154@qq.com>
 */

@Data
@AllArgsConstructor
public class JwtUserDto implements UserDetails {

    private User user;

    private String username;

    private String password;

    private List<Object> role;

    private List<GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
