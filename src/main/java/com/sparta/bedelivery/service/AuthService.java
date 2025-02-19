package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.AuthRequest;
import com.sparta.bedelivery.dto.AuthResponse;
import com.sparta.bedelivery.dto.ChangePasswordRequest;
import com.sparta.bedelivery.dto.UserRegisterRequest;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // 회원가입
    @Transactional
    public User registerUser(UserRegisterRequest request) {
        if (userRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        if (userRepository.findByNickname(request.getNickName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        User user = new User(request,passwordEncoder);
        return userRepository.save(user);
    }

    // 로그인
    public AuthResponse login(AuthRequest request) {
        // 1. 요청한 UserId로 User 엔티티 조회
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 비밀번호 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
        // 3. UserDetails 대신 User 엔티티를 사용하여 Role 추출
        String role = user.getRole().name(); // User 엔티티의 Role을 직접 가져옵니다.

        // 4. JWT 토큰 생성 (Role 포함)
        String token = jwtUtil.generateAccessToken(user.getUserId(), role);

        // 5. 생성된 토큰과 Role을 응답으로 반환
        return new AuthResponse(token, role);
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(request,passwordEncoder);
    }
}
