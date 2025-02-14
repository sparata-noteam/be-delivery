package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.UserAddressRepository;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    // 사용자 정보 조회
    public UserResponse getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserResponse(user);
    }

    // 사용자 정보 수정
    @Transactional
    public UserResponse updateUser(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.updateInfo(request.getName(), request.getPhone());
        return new UserResponse(user);
    }

    // 사용자 탈퇴
    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        user.delete(deletedBy); // 소프트 삭제 처리
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(user);
    }


}
