package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.ChangePasswordRequest;
import com.sparta.bedelivery.dto.UserRegisterRequest;
import com.sparta.bedelivery.dto.UserResponse;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 1.1 회원가입
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @PostMapping("/register")
    public ResponseEntity<ApiResponseData<UserResponse>> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = authService.registerUser(request);
        UserResponse userResponse = new UserResponse(user); // userResponse는 userId를 포함해야 함

        ApiResponseData<UserResponse> response = ApiResponseData.success(userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 1.3 로그아웃 (JWT 기반이라 서버에서 별도 로직 없음)
    @Operation(summary = "로그아웃", description = "JWT 기반 로그아웃 처리")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseData<?>> logout() {
        ApiResponseData<?> response = ApiResponseData.success(Map.of("success", true));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 1.7 비밀번호 변경
    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호와 새로운 비밀번호를 사용하여 비밀번호를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공")
    @PostMapping("/me/password")
    public ResponseEntity<ApiResponseData<?>> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                             @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userDetails.getUsername(), request);
        ApiResponseData<?> response = ApiResponseData.success(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
