package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.OrderService;
import com.sparta.bedelivery.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderPaymentController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    //4.8 전체 주문 목록 조회(관리자용)
    @GetMapping("/orders")
    public ResponseEntity<ApiResponseData<List<AdminOrderListResponse>>> getOrders() {
        return ResponseEntity.ok(ApiResponseData.success(orderService.getOrderList()));
    }

    // 4.9 주문 상태 강제 변경 (관리자)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponseData<ChangeForceStatusResponse>> updateOrderStatus(@PathVariable String orderId,
                                                                                        @RequestBody OrderForceStatusRequest forceChange) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.forceChange(UUID.fromString(orderId), forceChange.getStatus())));
    }

    // 주문내역 삭제
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponseData<?>> deleteOrders(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String orderId) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(orderService.deleteOrder(loginUser, UUID.fromString(orderId))));
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

    // 결제 내역 삭제
    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<ApiResponseData<?>> deletePayments(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String paymentId) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(paymentService.deletePayment(loginUser, UUID.fromString(paymentId))));
    }

}
