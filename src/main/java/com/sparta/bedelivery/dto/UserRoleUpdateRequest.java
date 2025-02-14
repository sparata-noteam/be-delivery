package com.sparta.bedelivery.dto;

import lombok.Data;

@Data
public class UserRoleUpdateRequest { //사용자 권한 변경 DTO
    private String role;
}