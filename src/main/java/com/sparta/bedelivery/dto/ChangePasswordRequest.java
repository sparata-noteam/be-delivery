package com.sparta.bedelivery.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest {
    private String email;
    private String currentPassword;
    private String newPassword;
}
