package com.sparta.bedelivery.dto;

import lombok.Data;

@Data
public class UserUpdateRequest { //사용자 정보 수정 DTO
    private String name;
    private String phone;
}