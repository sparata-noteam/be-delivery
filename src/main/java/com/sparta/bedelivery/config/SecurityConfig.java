package com.sparta.bedelivery.config;

import com.sparta.bedelivery.security.JwtAuthenticationFilter;
import com.sparta.bedelivery.security.JwtAuthorizationFilter;
import com.sparta.bedelivery.security.JwtUtil;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/users/login"); // 로그인 요청 URL 설정

        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtil, userDetailsService, authenticationManager());

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("MASTER") // URL 패턴에서도 권한 설정
                        .anyRequest().permitAll() // 암호처리해야함..
                )
                // 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가 (로그인 필터 역할)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 권한 필터를 인증 필터 뒤에 추가
                .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
