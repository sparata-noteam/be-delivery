package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.AdminService;
import com.sparta.bedelivery.service.OrderService;
import com.sparta.bedelivery.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;
    private final PaymentService paymentService;
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
    ) {
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


    //4.8 전체 주문 목록 조회(관리자용)
    @GetMapping("/orders")
    public ResponseEntity<ApiResponseData<List<AdminOrderListResponse>>> getOrders() {
        return ResponseEntity.ok(ApiResponseData.success(orderService.getOrderList()));
    }

    // 4.9 주문 상태 강제 변경 (관리자)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponseData<?>> updateOrderStatus(@PathVariable String orderId,
                                                                @RequestBody OrderChangeStatus status) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.status(UUID.fromString(orderId), status.getStatus())));
    }


    //    5.4 전체 결제 목록 조회
    @GetMapping("/payments")
    public ResponseEntity<ApiResponseData<List<AdminPaymentResponse>>> getPayments() {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.adminPaymentList()));
    }

    //5.5 특정 결제 상세 조회
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<ApiResponseData<AdminPaymentDetailResponse>> getPaymentDetail(@PathVariable String paymentId) {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.paymentDetail(UUID.fromString(paymentId))));
    }

    //5.6 사용자 환불 요청 승인(관리자)
    @PutMapping("/payments/{paymentId}/refund")
    public ResponseEntity<ApiResponseData<?>> refundOk(@PathVariable String paymentId) {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.refundSuccess(UUID.fromString(paymentId)), "환불처리 승인하였습니다"));
    }

}
