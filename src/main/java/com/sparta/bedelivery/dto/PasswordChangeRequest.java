package com.sparta.bedelivery.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest { //비밀번호 변경 요청 DTO
    private String currentPassword;
    private String newPassword;
}
