package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.order.*;
import com.sparta.bedelivery.dto.user.LoginUser;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "주문")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "주문을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "주문 생성 성공")
    @PreAuthorize("hasAnyRole('MASTER','CUSTOMER','OWNER','MANAGER')")
    @PostMapping()
    public ResponseEntity<ApiResponseData<CreateOrderResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateOrderRequest createOrderRequest) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(orderService.create(loginUser, createOrderRequest)));
    }


    //o,m 주문 확인
    @Operation(summary = "주문 확인", description = "점주(매니저)가 주문을 확인합니다.")
    @ApiResponse(responseCode = "200", description = "주문 확인 성공")
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PutMapping("/{orderId}/accept")
    public ResponseEntity<ApiResponseData<OrderAcceptResponse>> accept(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId) {
        LoginUser loginUser = new LoginUser(userDetails);
        return ResponseEntity.ok(ApiResponseData.success(orderService.accept(UUID.fromString(orderId))));
    }

    //주문 취소
    @Operation(summary = "주문 취소", description = "주문을 취소합니다.")
    @ApiResponse(responseCode = "200", description = "주문 취소 성공")
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
    @Operation(summary = "주문 상태 변경", description = "현재 주문의 상태를 변경합니다. 배달중, 배달완료")
    @ApiResponse(responseCode = "200", description = "주문 상태 변경 성공")
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponseData<OrderStatusResponse>> status(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId,
            @RequestBody OrderChangeStatus changeStatus) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.status(UUID.fromString(orderId), changeStatus.getStatus())));
    }

    // 목록 조회 (사용자용)
    @Operation(summary = "주문 목록 조회(사용자용)", description = "주문을 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping()
    public ResponseEntity<ApiResponseData<CustomerOrderResponse>> getList(@AuthenticationPrincipal UserDetails userDetails,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(required = false) String storeId,
                                                                          @RequestParam(required = false) Order.OrderStatus status) {
        LoginUser loginUser = new LoginUser(userDetails);
        Pageable pageable = PageRequest.of(page - 1, size);
        CustomerOrderRequest request = new CustomerOrderRequest(loginUser.getUserId(), storeId, status);
        return ResponseEntity.ok(ApiResponseData.success(orderService.getCustomerOrderList(pageable, request)));
    }

    // 목록 조회 (점주용)
    @Operation(summary = "주문 목록 조회", description = "주문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponseData<OwnerOrderListResponse>> getForOwner(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String storeId,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size,
                                                                               @RequestParam(required = false) Order.OrderStatus status) {
        LoginUser loginUser = new LoginUser(userDetails);
        Pageable pageable = PageRequest.of(page - 1, size);
        OwnerOrderRequest request = new OwnerOrderRequest(storeId, status);

        return ResponseEntity.ok(ApiResponseData.success(orderService.getOwnerOrderList(pageable, request)));
    }


    // 상세 조회
    // all
    @Operation(summary = "주문 상세 조회", description = "주문 상세 조회를 합니다.")
    @ApiResponse(responseCode = "200", description = "주문 상세 조회 성공")
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponseData<OrderDetailResponse>> getDetail(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponseData.success(orderService.getDetails(orderId)));
    }
}
