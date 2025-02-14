package com.sparta.bedelivery.dto;

import lombok.Data;

@Data
public class AuthRequest { //로그인 요청 DTO
    private String userId;
    private String password;
}
