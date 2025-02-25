package com.sparta.bedelivery.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bedelivery.dto.user.RoleUpdateRequest;
import com.sparta.bedelivery.dto.user.UserResponse;
import com.sparta.bedelivery.entity.Review;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.ReviewRepository;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.dto.review.AdminReviewResponse;
import java.util.UUID;
import com.sparta.bedelivery.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final JPAQueryFactory queryFactory;


    // 사용자 목록 조회
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        QUser qUser = QUser.user;  // QueryDSL로 생성된 Q 클래스
        BooleanExpression predicate = qUser.deleteAt.isNull();  // 삭제되지 않은 사용자만 조회

        // JPAQuery 생성
        JPAQuery<User> query = queryFactory.selectFrom(qUser)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<User> users = query.fetch();  // 조회된 사용자 리스트
        long total = query.fetchCount();  // 전체 사용자 수 (페이징을 위한 총 개수)

        return new PageImpl<>(users.stream()
                .map(UserResponse::new)  // User -> UserResponse 변환
                .collect(Collectors.toList()), pageable, total);
    }
    //    public Page<UserResponse> getAllUsers(Pageable pageable) {
//        return userRepository.findAll(pageable)
//                .map(UserResponse::new);
//    }




    // 특정 사용자 상세 조회 (삭제되지 않은 사용자만)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .filter(u -> u.getDeleteAt() == null)  // 삭제되지 않은 사용자만 필터링
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserResponse(user); // UserResponse의 생성자에 맞춰 user 엔티티를 전달
    }


    // 특정 사용자 강제 탈퇴
    @Transactional
    public void deleteUserByAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .filter(u -> u.getDeleteAt() == null)  // 삭제되지 않은 사용자만 필터링
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        user.delete(deletedBy); // 소프트 삭제 처리
    }

    // 사용자 권한 변경
    @Transactional
    public void updateUserRole(Long userId, RoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .filter(u -> u.getDeleteAt() == null)  // 삭제되지 않은 사용자만 필터링
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        try {
            User.Role newRole = User.Role.valueOf(request.getRole());
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 역할 값입니다: " + request.getRole());
        }
    }

    // 리뷰 전체 조회
    @Transactional(readOnly = true)
    public Page<AdminReviewResponse> getAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Review> reviews = reviewRepository.findAllByDeleteAtIsNull(pageable);
        return reviews.map(AdminReviewResponse::new);
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
