package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.user.AuthResponse;
import com.sparta.bedelivery.dto.user.ChangePasswordRequest;
import com.sparta.bedelivery.dto.user.UserRegisterRequest;
import com.sparta.bedelivery.dto.user.UserResponse;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.security.JwtBlacklistService;
import com.sparta.bedelivery.security.JwtUtil;
import com.sparta.bedelivery.service.AuthService;
import com.sparta.bedelivery.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "보안")
public class AuthController {

    private final AuthService authService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

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

//     리프레시 토큰을 사용한 액세스 토큰 재발급
    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            String userId = jwtUtil.parseToken(refreshToken).getSubject();

            // 저장된 리프레시 토큰과 일치하는지 확인
            String storedRefreshToken = jwtBlacklistService.getRefreshToken(userId);
            if (!refreshToken.equals(storedRefreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseData.failure(401, "유효하지 않은 리프레시 토큰"));
            }

            UserDetails userDetails = userService.loadUserByUsername(userId);
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElseThrow(() -> new IllegalStateException("사용자의 역할 정보를 찾을 수 없습니다."));


            String newAccessToken = jwtUtil.generateAccessToken(userDetails.getUsername(), role);

            return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken, role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseData.failure(401, "리프레시 토큰이 유효하지 않습니다."));
        }
    }

    // 1.3 로그아웃 (JWT 기반)
    @Operation(summary = "로그아웃", description = "JWT 기반 로그아웃 처리")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseData<String>> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.badRequest().body(ApiResponseData.failure(400, "유효하지 않은 토큰입니다."));
        }

        long expirationMillis = jwtUtil.getExpirationTime(token) - System.currentTimeMillis();
        jwtBlacklistService.addToBlacklist(token, expirationMillis);
        // 리프레시 토큰도 삭제
        String userId = jwtUtil.parseToken(token).getSubject();
        jwtBlacklistService.deleteRefreshToken(userId);

        return ResponseEntity.ok(ApiResponseData.success("로그아웃 성공"));
    }

    /**
     * 요청에서 JWT 추출
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

//    public ResponseEntity<ApiResponseData<?>> logout() {
//        ApiResponseData<?> response = ApiResponseData.success(Map.of("success", true));
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

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
