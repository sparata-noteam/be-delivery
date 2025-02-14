package com.sparta.bedelivery.dto;

import lombok.Data;

@Data
public class UserRegisterRequest { //회원가입 요청
    private String username;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String role;
}
