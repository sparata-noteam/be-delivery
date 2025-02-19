package com.sparta.bedelivery.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.dto.AuthRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    /**
     * 로그인 요청을 가로채서 인증 수행
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            AuthRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authRequest.getUserId(), authRequest.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 파싱 실패", e);
        }
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

        String token = jwtUtil.generateAccessToken(userDetails.getUsername(), role);

        response.setHeader("Authorization", "Bearer " + token);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        PrintWriter writer = response.getWriter();
//        writer.write(new ObjectMapper().writeValueAsString(new AuthResponse(token)));
//        writer.flush();
    }

    /**
     * 인증 실패 시 응답 처리
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Authentication Failed: " + failed.getMessage());
    }
}
