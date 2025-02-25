package com.sparta.bedelivery.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.dto.user.AuthRequest;
import com.sparta.bedelivery.dto.user.AuthResponse;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtBlacklistService jwtBlacklistService;
//    private final Map<String, Integer> failedAttempts = new HashMap<>();



    /**
     * 로그인 요청을 가로채서 인증 수행
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            AuthRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);
            // 사용자가 존재하는지 확인 및 delete_at 체크
            User user = userRepository.findByUserId(authRequest.getUserId())
                    .orElseThrow(() -> new AuthenticationException("404: 해당 계정이 존재하지 않습니다.") {});

            if (user.getDeleteAt() != null) {
                throw new AuthenticationException("403: 해당 계정은 삭제된 상태입니다.") {};
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authRequest.getUserId(), authRequest.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 파싱 실패", e);
        }
//        AuthRequest authRequest = null; // try 블록 밖에서 선언
//
//        try {
//            authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);
//            String userId = authRequest.getUserId();
//
//            // 로그인 실패 횟수 체크
//            if (failedAttempts.getOrDefault(userId, 0) >= 5) {
//                throw new AuthenticationException("403: 너무 많은 로그인 시도로 인해 계정이 잠겼습니다.") {};
//            }
//
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(userId, authRequest.getPassword())
//            );
//
//            // 로그인 성공 시 실패 횟수 초기화
//            failedAttempts.remove(userId);
//            return authentication;
//        } catch (AuthenticationException e) {
//            // 로그인 실패 시 횟수 증가
//            if (authRequest != null) {
//                String userId = authRequest.getUserId();
//                failedAttempts.put(userId, failedAttempts.getOrDefault(userId, 0) + 1);
//            }
//            throw e;
//        } catch (IOException e) {
//            throw new RuntimeException("로그인 요청 파싱 실패", e);
//        }
    }

    /**
     * 인증 성공 시 JWT 생성 후 응답
     */
    // controller 에서 로그인을 안쓰는 경우에 사용
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String role = ((CustomUserDetails) userDetails).getUser().getRole().name();

//        String token = jwtUtil.generateAccessToken(userDetails.getUsername(), role);
        // 액세스 토큰과 리프레시 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername(), role);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        // 리프레시 토큰을 Redis에 저장 (7일 동안 유지)
        jwtBlacklistService.storeRefreshToken(userDetails.getUsername(), refreshToken, 7);

//        response.setHeader("Authorization", "Bearer " + token);
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(new AuthResponse(accessToken, refreshToken, role)));
        writer.flush();
    }

    /**
     * 인증 실패 시 응답 처리
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        String errorMessage = "{\"message\":\"Authentication Failed: " + failed.getMessage() + "\"}";
//        response.getWriter().write(errorMessage);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponseData<String> responseData = ApiResponseData.failure(401, "로그인 실패: " + failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
    }
}
