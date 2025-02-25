package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse { //로그인 응답 DTO
    private String accessToken;
    private String refreshToken;
    private String role;
}
