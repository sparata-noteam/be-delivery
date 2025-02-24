package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.RoleUpdateRequest;
import com.sparta.bedelivery.dto.UserResponse;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.dto.AdminReviewResponse;
import com.sparta.bedelivery.service.AdminService;
import java.util.UUID;
import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final StoreService storeService;
//    1. Spring Security가 SecurityContextHolder에서 현재 사용자의 Authentication 정보 확인
//    2. getAuthorities()에서 ROLE_ADMIN이 있는지 체크
//    3. 없으면 AccessDeniedException 발생 (403 Forbidden)

    // 1.8 사용자 목록 조회
    @Tag(name = "계정")
    @Operation(summary = "사용자 목록 조회", description = "삭제되지 않은 사용자 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공")
    @GetMapping("/users")
    public ResponseEntity<ApiResponseData<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("🔍 현재 SecurityContext 사용자: " + authentication.getPrincipal());
//        System.out.println("🔍 현재 SecurityContext 권한: " + authentication.getAuthorities());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<UserResponse> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponseData.success(users));
    }

    // 1.9 특정 사용자 상세 조회
    @Tag(name = "계정")
    @Operation(summary = "특정 사용자 상세 조회", description = "특정 사용자의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 상세 조회 성공")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponseData<UserResponse>> getUserById(@PathVariable Long userId) {
        UserResponse user = adminService.getUserById(userId);
        return ResponseEntity.ok(ApiResponseData.success(user));
    }

    // 1.10 특정 사용자 강제 탈퇴
    @Tag(name = "계정")
    @Operation(summary = "특정 사용자 강제 탈퇴", description = "관리자가 사용자를 강제 탈퇴시킵니다.")
    @ApiResponse(responseCode = "200", description = "사용자 탈퇴 성공")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponseData<Void>> deleteUserByAdmin(@PathVariable Long userId) {
        adminService.deleteUserByAdmin(userId);
        return ResponseEntity.ok(ApiResponseData.success(null, "사용자가 삭제되었습니다."));
    }

    // 1.11 사용자 권한 변경
    @Tag(name = "계정")
    @Operation(summary = "사용자 권한 변경", description = "관리자가 특정 사용자의 권한을 변경합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 권한 변경 성공")
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<ApiResponseData<Void>> updateUserRole(@PathVariable Long userId, @RequestBody RoleUpdateRequest request) {
        adminService.updateUserRole(userId, request);
        return ResponseEntity.ok(ApiResponseData.success(null, "User role updated successfully"));
    }

// ================================================= 매 장 ==========================================================

    // 3.6 전체 매장 목록 조회 (관리자용)
    @Tag(name = "매장")
    @Operation(summary = "전체 매장 목록 조회 (관리자용)", description = "전체 매장을 조회합니다.")
    @GetMapping("/stores/{userId}")
    public ResponseEntity<ApiResponseData<List<StoreStatusResponseDto>>> findAllStores(@PathVariable String userId) {
        List<StoreStatusResponseDto> findStore = storeService.findAllStores(userId);

        return ResponseEntity.ok().body(ApiResponseData.success(findStore));
    }

    @Tag(name = "매장")
    // 3.7 특정 매장 강제 삭제
    @Operation(summary = "특정 매장 강제 삭제 (관리자)", description = "특정 매장 강제 삭제 합니다.")
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponseData<Void>> deleteStore(@PathVariable UUID storeId) {
        storeService.deleteStore(storeId);

        return ResponseEntity.ok().body(ApiResponseData.success(null,"매장이 강제 삭제되었습니다."));
    }

    @Tag(name = "매장")
    // 3.8 매장 등록, 삭제요청 승인
    @Operation(summary = "매장 등록, 삭제요청 승인 (관리자)", description = "매장 등록, 삭제요청 승인 합니다.")
    @PutMapping("/stores/{storeId}/approve")
    public ResponseEntity<ApiResponseData<StoreStatusResponseDto>> approveStore(@PathVariable UUID storeId){
        StoreStatusResponseDto approve = storeService.approveStore(storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseData.success(
                approve, "승인되었습니다."
        ));
    }
    @Tag(name = "매장")

    // 3.9 매장 정보 수정 승인 (관리자용)
    @Operation(summary = "매장 정보 수정 승인 (관리자)", description = "매장 정보 수정을 승인 합니다.")
    @PutMapping("/stores/{storeId}/update")
    public ResponseEntity<ApiResponseData<StoreStatusResponseDto>> updateStore(@PathVariable UUID storeId) {

        StoreStatusResponseDto update = storeService.updateStore(storeId);

        return ResponseEntity.ok().body(ApiResponseData.success(update, "매장 정보 수정이 승인되었습니다."));
    }

    // 3.10 매장 정보 등록 (관리자용)
    @Tag(name = "매장")
    @Operation(summary = "매장 정보 등록 (관리자)", description = "매장 정보 등록을 합니다.")
    @PostMapping("/stores")
    public ResponseEntity<ApiResponseData<CreateStoreResponseDto>> createStore(@RequestBody CreateStoreRequestDto requestDto) {
        CreateStoreResponseDto createStore = storeService.createStore(requestDto);

        return ResponseEntity.ok().body(ApiResponseData.success(createStore, "매장이 성공적으로 등록되었습니다."));
    }

// ================================================= 리 뷰 ==========================================================

    //6.6 전체 리뷰 조회
    @Tag(name = "리뷰")
    @Operation(summary = "전체 리뷰 조회(관리자)", description = "관리자가 전체 리뷰를 최신순으로 조회 합니다.")
    @GetMapping("/admin/reviews")
    public ResponseEntity<ApiResponseData<Page<AdminReviewResponse>>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<AdminReviewResponse> reviews = adminService.getAllReviews(page, size);
        return ResponseEntity.ok(ApiResponseData.success(reviews, "전체 리뷰가 조회되었습니다."));
    }

    //6.7 특정 리뷰 삭제
    @Tag(name = "리뷰")
    @DeleteMapping("/admin/{reviewId}")
    @Operation(summary = "특정 리뷰 삭제(관리자)", description = "관리자가 특정 리뷰를 삭제합니다.")
    public ResponseEntity<ApiResponseData<String>> deleteReviewByAdmin(@PathVariable UUID reviewId) {
        adminService.deleteReviewByAdmin(reviewId);
        return ResponseEntity.ok(ApiResponseData.success(null, "관리자가 리뷰를 삭제했습니다."));
    }

}
