package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class LoginUser {
    private final String userId;
    private final User.Role role;
    public LoginUser(String userId, GrantedAuthority authority) {
        this.userId = userId;
        this.role = User.Role.findRole(authority.getAuthority());
    }
}
