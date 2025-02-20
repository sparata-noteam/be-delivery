package com.sparta.bedelivery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.security.JwtAuthenticationFilter;
import com.sparta.bedelivery.security.JwtAuthorizationFilter;
import com.sparta.bedelivery.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/users/login");

        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtil, userDetailsService, authenticationManager());

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("MASTER")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger 경로에 대한 인증 허용
                        .requestMatchers("/error").permitAll() // 404 처리를 위해 Spring Boot의 기본 예외 처리 허용
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint())  // 401 처리
                        .accessDeniedHandler(customAccessDeniedHandler())  // 403 처리
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);
                // 필터 순서 조정: Spring Boot가 404를 먼저 처리할 수 있도록 필터를 변경
//                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationFilter, JwtAuthorizationFilter.class);

        return http.build();
    }

    // 401 Unauthorized - 인증되지 않은 요청
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response,
                authException) -> sendJsonResponse(response, 401, "인증이 필요합니다.");
    }

    // 403 Forbidden - 권한이 부족한 요청
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response,
                accessDeniedException) -> sendJsonResponse(response, 403, "접근 권한이 없습니다.");
    }

    private void sendJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // UTF-8 설정

        ApiResponseData<String> responseData = ApiResponseData.failure(status, message);

        response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
    }
}
