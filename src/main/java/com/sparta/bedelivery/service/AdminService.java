package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.RoleUpdateRequest;
import com.sparta.bedelivery.dto.UserResponse;
import com.sparta.bedelivery.entity.Review;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.review.repository.ReviewRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    // 사용자 목록 조회
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserResponse::new);
    }


    // 특정 사용자 상세 조회
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserResponse(user); // UserResponse의 생성자에 맞춰 user 엔티티를 전달
    }


    // 특정 사용자 강제 탈퇴
    @Transactional
    public void deleteUserByAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));


        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        user.delete(deletedBy); // 소프트 삭제 처리
    }

    // 사용자 권한 변경
    @Transactional
    public void updateUserRole(Long userId, RoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        try {
            User.Role newRole = User.Role.valueOf(request.getRole());
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 역할 값입니다: " + request.getRole());
        }
    }

    // 특정 리뷰 삭제
    @Transactional
    public void deleteReviewByAdmin(UUID reviewId) {
        // 리뷰 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        // 이미 삭제된 리뷰인지 확인
        if (review.getDeleteAt() != null) {
            throw new IllegalArgumentException("이미 삭제된 리뷰입니다.");
        }

        // 소프트 삭제 처리
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        review.delete(deletedBy);
        reviewRepository.save(review);
    }

}
