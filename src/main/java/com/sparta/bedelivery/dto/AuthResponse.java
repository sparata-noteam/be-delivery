package com.sparta.bedelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse { //로그인 응답 DTO
    private String token;
}
