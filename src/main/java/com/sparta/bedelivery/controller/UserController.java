package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.security.CustomUserDetails;
import com.sparta.bedelivery.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 내 정보가 조회되었습니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponseData<UserResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponseData.success(userService.getUserInfo(userDetails.getUsername())));
    }

    // 1.5 내 정보 수정
    @Operation(summary = "내 정보 수정", description = "현재 로그인한 사용자의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 내 정보가 수정되었습니다.")
    @PutMapping("/me")
    public ResponseEntity<ApiResponseData<UserResponse>> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponseData.success(userService.updateUser(userDetails.getUsername(), request)));
    }

    // 1.6 사용자 탈퇴
    @Operation(summary = "사용자 탈퇴", description = "현재 로그인한 사용자가 탈퇴를 요청합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 사용자 계정이 삭제되었습니다.")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponseData<String>> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseData.success(userDetails.getUsername() + " 계정이 삭제되었습니다."));
    }

}
