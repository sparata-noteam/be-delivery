package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "결제")
public class PaymentController {

    private final PaymentService paymentService;


    //5.1 결제 요청
    @Operation(summary = "결제 진행", description = "결제를 진행합니다.")
    @ApiResponse(responseCode = "200", description = "결제 진행 성공")
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<ApiResponseData<CreatePaymentResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreatePaymentRequest createPaymentRequest) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(paymentService.create(loginUser, createPaymentRequest)));
    }


    //5.2 환불 요청
    @Operation(summary = "환불 요청", description = "고객이 환불을 요청합니다.")
    @ApiResponse(responseCode = "200", description = "환불 요청")
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{orderId}/refund")
    public ResponseEntity<ApiResponseData<PaymentRefundResponse>> refund(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.refund(UUID.fromString(orderId)), "주문이 환불 요청되었습니다."));
    }

    //#5.3 결제 취소
    @Operation(summary = "결제 취소", description = "현재 진행하고 있는 결제를 취소합니다.")
    @ApiResponse(responseCode = "200", description = "결제 취소 성공")
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PutMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponseData<PaymentCancelResponse>> cancel(@PathVariable String paymentId) {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.cancel(UUID.fromString(paymentId)), "결제 취소요청되었습니다"));
    }


}
