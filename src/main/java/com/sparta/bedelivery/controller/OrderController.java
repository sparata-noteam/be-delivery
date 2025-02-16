package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.CreateOrderRequest;
import com.sparta.bedelivery.dto.CreateOrderResponse;
import com.sparta.bedelivery.dto.LoginUser;
import com.sparta.bedelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

        String userId = userDetails.getUsername();
        GrantedAuthority authority = userDetails.getAuthorities().stream().toList().get(0);
        LoginUser loginUser = new LoginUser(userId, authority);
        return ResponseEntity.ok(orderService.create(loginUser, createOrderRequest));
    }

    //o,m
    @PutMapping("/{orderId}/accept")
    public void accept() {

    }

    //상태 변경 o m
    @PutMapping("/{orderId}/status")
    public void status() {

    }

    // 목록 조회 (사용자용)
    @GetMapping("/{userId}")
    public void get() {

    }

    // 목록 조회 (점주용)
    @GetMapping("/{storeId}")
    public void getForOwner() {

    }

    // 상세 조회 (점주용)
    // all
    @GetMapping("/{orderId}")
    public void getDetail() {

    }

    // 상세 조회 (점주용)
    // c,o,m
    @PutMapping("/{orderId}/cancel")
    public void cancel() {

    }

    // admin/orders/ get
    // admin/orders/{orderId}/status put


}
