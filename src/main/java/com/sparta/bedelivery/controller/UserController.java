package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.security.CustomUserDetails;
import com.sparta.bedelivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1.4 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponseData<UserResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponseData.success(userService.getUserInfo(userDetails.getUsername())));
    }

    // 1.5 내 정보 수정
    @PutMapping("/me")
    public ResponseEntity<ApiResponseData<UserResponse>> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponseData.success(userService.updateUser(userDetails.getUsername(), request)));
    }

    // 1.6 사용자 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponseData<String>> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseData.success(userDetails.getUsername() + " 계정이 삭제되었습니다."));
    }

}
