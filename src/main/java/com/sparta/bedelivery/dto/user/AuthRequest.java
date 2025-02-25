package com.sparta.bedelivery.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest { //로그인 요청 DTO
    private String userId;
    private String password;
}
