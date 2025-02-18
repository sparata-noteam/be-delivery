package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 1.1 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponseData<UserResponse>> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = authService.registerUser(request);
        UserResponse userResponse = new UserResponse(user); // userResponse는 userId를 포함해야 함

        ApiResponseData<UserResponse> response = ApiResponseData.success(userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 1.3 로그아웃 (JWT 기반이라 서버에서 별도 로직 없음)
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseData<?>> logout() {
        ApiResponseData<?> response = ApiResponseData.success(Map.of("success", true));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 1.7 비밀번호 변경
    @PostMapping("/me/password")
    public ResponseEntity<ApiResponseData<?>> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userDetails.getUsername(), request);
        ApiResponseData<?> response = ApiResponseData.success(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
