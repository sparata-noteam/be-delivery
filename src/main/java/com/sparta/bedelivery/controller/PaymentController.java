package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    //5.1 결제 요청
    @PostMapping
    public ResponseEntity<ApiResponseData<CreatePaymentResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreatePaymentRequest createPaymentRequest) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(paymentService.create(loginUser, createPaymentRequest)));
    }


    //5.2 환불 요청
    @PutMapping("/{orderId}/refund")
    public ResponseEntity<ApiResponseData<PaymentRefundResponse>> refund(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.refund(UUID.fromString(orderId)), "주문이 환불 요청되었습니다."));
    }

    //#5.3 결제 취소
    @PutMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponseData<PaymentCancelResponse>> cancel(@PathVariable String paymentId) {
        return ResponseEntity.ok(ApiResponseData.success(paymentService.cancel(UUID.fromString(paymentId)), "결제 취소요청되었습니다"));
    }


}
