package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.order.AdminOrderCondition;
import com.sparta.bedelivery.dto.order.AdminOrderListResponse;
import com.sparta.bedelivery.dto.order.ChangeForceStatusResponse;
import com.sparta.bedelivery.dto.order.OrderForceStatusRequest;
import com.sparta.bedelivery.dto.payment.AdminPaymentCondition;
import com.sparta.bedelivery.dto.payment.AdminPaymentDetailResponse;
import com.sparta.bedelivery.dto.payment.AdminPaymentListResponse;
import com.sparta.bedelivery.dto.user.LoginUser;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Payment;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.OrderService;
import com.sparta.bedelivery.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderPaymentController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    @Tag(name = "주문")
    @Operation(summary = "주문 목록 조회(관리자용)", description = "주문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    //4.8 전체 주문 목록 조회(관리자용)
    @GetMapping("/orders")
    public ResponseEntity<ApiResponseData<AdminOrderListResponse>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Order.OrderStatus status,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) Boolean isDeleted
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        AdminOrderCondition condition = new AdminOrderCondition(status, storeId, isDeleted);
        return ResponseEntity.ok(ApiResponseData.success(orderService.getOrderList(pageable, condition)));
    }


    @Tag(name = "주문")
    @Operation(summary = "주문 상태 강제 변경(관리자)", description = "주문 상태를 변경합니다.(관리자)")
    @ApiResponse(responseCode = "200", description = "주문 상태 변경 성공")
    // 4.9 주문 상태 강제 변경 (관리자)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponseData<ChangeForceStatusResponse>> updateOrderStatus(@PathVariable String orderId,
                                                                                        @RequestBody OrderForceStatusRequest forceChange) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.forceChange(UUID.fromString(orderId), forceChange.getStatus())));
    }

    @Tag(name = "주문")
    @Operation(summary = "주문 내역 삭제", description = "주문 내역을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "주문 내역 삭제 성공")
    // 주문내역 삭제
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponseData<?>> deleteOrders(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String orderId) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(orderService.deleteOrder(loginUser, UUID.fromString(orderId))));
    }

    @Tag(name = "결제")
    @Operation(summary = "결제 목록 조회", description = "결제를 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "결제 목록 조회")
    //    5.4 전체 결제 목록 조회
    @GetMapping("/payments")
    public ResponseEntity<ApiResponseData<AdminPaymentListResponse>> getPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Payment.Status status,
            @RequestParam(required = false) Payment.Method method,
            @RequestParam(required = false) Boolean isDeleted
    ) {
        Pageable pageable = PageRequest.of(page, size);
        AdminPaymentCondition condition = new AdminPaymentCondition(status, method, isDeleted);
        return ResponseEntity.ok(ApiResponseData.success(paymentService.adminPaymentList(pageable, condition)));
    }

    @Tag(name = "결제")
    @Operation(summary = "결제 상세 조회", description = "특정 결제의 상세 조회합니다.")
    @ApiResponse(responseCode = "200", description = "결제 상세 조회 성공")
    //5.5 특정 결제 상세 조회
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<ApiResponseData<AdminPaymentDetailResponse>> getPaymentDetail(@PathVariable String paymentId) {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.paymentDetail(UUID.fromString(paymentId))));
    }

    @Tag(name = "결제")
    @Operation(summary = "결제 환불 요청 승인(관리자)", description = "결제 환불 승인")
    @ApiResponse(responseCode = "200", description = "결제 환불 성공")
    //5.6 사용자 환불 요청 승인(관리자)
    @PutMapping("/payments/{paymentId}/refund")
    public ResponseEntity<ApiResponseData<?>> refundOk(@PathVariable String paymentId) {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.refundSuccess(UUID.fromString(paymentId)), "환불처리 승인하였습니다"));
    }


}
