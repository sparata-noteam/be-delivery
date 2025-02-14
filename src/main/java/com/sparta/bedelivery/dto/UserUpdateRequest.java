package com.sparta.bedelivery.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateRequest { //사용자 정보 수정 DTO

    private String nickName;

    @Pattern(
            regexp = "^010\\d{8}$",
            message = "전화번호는 010으로 시작하는 11자리 숫자만 입력 가능합니다. 예) 01012345678"
    )
    private String phone;
}