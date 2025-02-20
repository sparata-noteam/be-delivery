package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.User;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class LoginUser {
    private final String userId;
    private final User.Role role;

    public LoginUser(String userId, User.Role role) {
        this.userId = userId;
        this.role = role;
    }

    public LoginUser(UserDetails userDetails) {
        this.userId = userDetails.getUsername();
        this.role = User.Role.findRole(userDetails.getAuthorities().stream().toList().get(0).getAuthority());
    }
}
