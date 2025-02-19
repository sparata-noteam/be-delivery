package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.RoleUpdateRequest;
import com.sparta.bedelivery.dto.UserResponse;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.review.dto.AdminReviewResponse;
import com.sparta.bedelivery.review.dto.StoreReviewResponse;
import com.sparta.bedelivery.security.CustomUserDetails;
import com.sparta.bedelivery.service.AdminService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

//    1. Spring Security가 SecurityContextHolder에서 현재 사용자의 Authentication 정보 확인
//    2. getAuthorities()에서 ROLE_ADMIN이 있는지 체크
//    3. 없으면 AccessDeniedException 발생 (403 Forbidden)

    // 1.8 사용자 목록 조회
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    )
    {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("🔍 현재 SecurityContext 사용자: " + authentication.getPrincipal());
//        System.out.println("🔍 현재 SecurityContext 권한: " + authentication.getAuthorities());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    // 1.9 특정 사용자 상세 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    // 1.10 특정 사용자 강제 탈퇴
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUserByAdmin(@PathVariable Long userId) {
        adminService.deleteUserByAdmin(userId);
        return ResponseEntity.ok("{\"message\": \"사용자가 삭제되었습니다.\"}");
    }

    // 1.11 사용자 권한 변경
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @RequestBody RoleUpdateRequest request) {
        adminService.updateUserRole(userId, request);
        return ResponseEntity.ok("{\"message\": \"User role updated successfully\"}");
    }

    //6.6 전체 리뷰 조회
    @GetMapping("/admin/reviews")
    public ResponseEntity<ApiResponseData<Page<AdminReviewResponse>>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<AdminReviewResponse> reviews = adminService.getAllReviews(page, size);
        return ResponseEntity.ok(ApiResponseData.success(reviews, "전체 리뷰가 조회되었습니다."));
    }
    
    //6.7 특정 리뷰 삭제
    @DeleteMapping("/admin/{reviewId}")
    public ResponseEntity<ApiResponseData<String>> deleteReviewByAdmin(@PathVariable UUID reviewId) {
        adminService.deleteReviewByAdmin(reviewId);
        return ResponseEntity.ok(ApiResponseData.success(null, "관리자가 리뷰를 삭제했습니다."));
    }



}
