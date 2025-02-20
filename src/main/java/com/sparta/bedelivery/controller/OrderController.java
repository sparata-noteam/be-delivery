package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER')")
    @PostMapping()
    public ResponseEntity<ApiResponseData<CreateOrderResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateOrderRequest createOrderRequest) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(orderService.create(loginUser, createOrderRequest)));
    }


    //o,m 주문 확인
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PutMapping("/{orderId}/accept")
    public ResponseEntity<ApiResponseData<OrderAcceptResponse>> accept(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(orderService.accept(UUID.fromString(orderId))));
    }

    //주문 취소
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER')")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponseData<OrderCancelResponse>> cancel(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(orderService.cancel(loginUser, UUID.fromString(orderId)),
                "주문이 취소가 되었습니다."));
    }

    //상태 변경 o m
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponseData<OrderStatusResponse>> status(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId,
            @RequestBody OrderChangeStatus changeStatus) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.status(UUID.fromString(orderId), changeStatus.getStatus())));
    }

    // 목록 조회 (사용자용)
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping()
    public ResponseEntity<ApiResponseData<CustomerOrderResponse>> getList(@AuthenticationPrincipal UserDetails userDetails,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(required = false) String storeId,
                                                                          @RequestParam(required = false) Order.OrderStatus status) {
        LoginUser loginUser = new LoginUser(userDetails);
        Pageable pageable = PageRequest.of(page, size);
        CustomerOrderRequest request = new CustomerOrderRequest(loginUser.getUserId(), storeId, status);
        return ResponseEntity.ok(ApiResponseData.success(orderService.getCustomerOrderList(pageable, request)));
    }

    // 목록 조회 (점주용)
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponseData<OwnerOrderListResponse>> getForOwner(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String storeId,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size,
                                                                               @RequestParam(required = false) Order.OrderStatus status) {
        LoginUser loginUser = new LoginUser(userDetails);
        Pageable pageable = PageRequest.of(page, size);
        OwnerOrderRequest request = new OwnerOrderRequest(storeId, status);

        return ResponseEntity.ok(ApiResponseData.success(orderService.getOwnerOrderList(pageable, request)));
    }


    // 상세 조회
    // all
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponseData<OrderDetailResponse>> getDetail(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.getDetails(orderId)));
    }
}
