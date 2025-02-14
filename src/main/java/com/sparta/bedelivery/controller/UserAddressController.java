package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/me/addresses")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    //2.1 배송지 추가
    @PostMapping
    public ResponseEntity<UserAddressResponse> addUserAddress(@AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody UserAddressRequest request) {
        return ResponseEntity.ok(userAddressService.addUserAddress(userDetails.getUsername(), request));
    }

    //2.2 배송지 목록 조회
    @GetMapping
    public ResponseEntity<List<UserAddressResponse>> getUserAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userAddressService.getUserAddresses(userDetails.getUsername()));
    }

    //2.3 배송지 수정
    @PutMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> updateUserAddress(@PathVariable UUID addressId,
                                                                 @RequestBody UserAddressRequest request) {
        return ResponseEntity.ok(userAddressService.updateUserAddress(addressId, request));
    }

    //2.4 배송지 삭제
    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable UUID  addressId) {
        userAddressService.deleteUserAddress(addressId);
        return ResponseEntity.ok("{\"message\": \"배송지가 삭제되었습니다.\"}");
    }

    //2.5 배송지 단건 조회
    @GetMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> getUserAddress(@PathVariable UUID  addressId) {
        return ResponseEntity.ok(userAddressService.getUserAddress(addressId));
    }
}
