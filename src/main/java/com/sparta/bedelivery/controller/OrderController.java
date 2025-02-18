package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // c,o,m
    @PostMapping()
    public ResponseEntity<CreateOrderResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateOrderRequest createOrderRequest) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(orderService.create(loginUser, createOrderRequest));
    }


    //o,m 주문 확인
    @PutMapping("/{orderId}/accept")
    public ResponseEntity<OrderAcceptResponse> accept(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(orderService.accept(UUID.fromString(orderId)));
    }

    //주문 취소
    // c,o,m
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponseData<OrderCancelResponse>> cancel(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.cancel(UUID.fromString(orderId)),
                "주문이 취소가 되었습니다."));
    }

    //상태 변경 o m
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponse> status(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId,
            @RequestBody OrderChangeStatus changeStatus) {
        return ResponseEntity.ok(orderService.status(UUID.fromString(orderId), changeStatus.getStatus()));
    }

    // 목록 조회 (사용자용)
    @GetMapping()
    public ResponseEntity<List<CustomerOrderResponse>> getList(@AuthenticationPrincipal UserDetails userDetails) {
        LoginUser loginUser = new LoginUser(userDetails);
        // 고객이 아니라면... 사용할 수 없다...
        if (loginUser.getRole() != User.Role.CUSTOMER) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderService.getCustomerOrderList(loginUser.getUserId()));
    }

    // 목록 조회 (점주용)
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<OwnerOrderResponse>> getForOwner(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String storeId) {
        LoginUser loginUser = new LoginUser(userDetails);

        if (loginUser.getRole() == User.Role.CUSTOMER) {
            throw new IllegalArgumentException("고객은 사용할 수 없는 API입니다.");
        }

        return ResponseEntity.ok(orderService.getOwnerOrderList(storeId));
    }

    // 상세 조회 (점주용)
    // all
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponseData<OrderDetailResponse>> getDetail(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.getDetails(orderId)));
    }


    // admin/orders/ get
    // admin/orders/{orderId}/status put


}
