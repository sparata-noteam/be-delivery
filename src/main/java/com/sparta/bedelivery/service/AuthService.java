package com.sparta.bedelivery.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bedelivery.dto.user.ChangePasswordRequest;
import com.sparta.bedelivery.dto.user.UserRegisterRequest;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final JPAQueryFactory queryFactory;


    // 회원가입
    @Transactional
    public User registerUser(UserRegisterRequest request) {
        if (userRepository.existsByUserIdAndDeleteAtIsNull(request.getUserId())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        if (userRepository.existsByNicknameAndDeleteAtIsNull(request.getNickName())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }if (userRepository.existsByPhoneAndDeleteAtIsNull(request.getPhone())) {
            throw new IllegalArgumentException("이미 존재하는 전화번호입니다.");
        }

        User user = new User(request,passwordEncoder);
        return userRepository.save(user);
    }


    // 비밀번호 변경
    @Transactional
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findByUserIdAndDeleteAtIsNull(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(request,passwordEncoder);
    }
}
