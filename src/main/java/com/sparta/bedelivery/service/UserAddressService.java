package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.UserAddressRequest;
import com.sparta.bedelivery.dto.UserAddressResponse;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.entity.UserAddress;
import com.sparta.bedelivery.repository.UserAddressRepository;
import com.sparta.bedelivery.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    // 배송지 추가
    @Transactional
    public UserAddressResponse addUserAddress(String userId, UserAddressRequest request) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserAddress address = new UserAddress(user, request.getAddressName(), request.getRecipientName(),
                request.getAddress(), request.getZipCode(), request.getIsDefault());

        userAddressRepository.save(address);
        return new UserAddressResponse(address);
    }

    // 특정 사용자의 배송지 목록 조회
    public List<UserAddressResponse> getUserAddresses(String userId) {
        List<UserAddress> addresses = userAddressRepository.findByUser_UserId(userId);
        return addresses.stream()
                .map(UserAddressResponse::new)
                .collect(Collectors.toList());
    }

    // 배송지 단건 조회
    public UserAddressResponse getUserAddress(UUID addressId) {
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("배송지를 찾을 수 없습니다."));
        return new UserAddressResponse(address);
    }

    // 배송지 수정
    @Transactional
    public UserAddressResponse updateUserAddress(UUID  addressId, UserAddressRequest request) {
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("배송지를 찾을 수 없습니다."));

        address.updateAddress(request.getAddressName(), request.getRecipientName(),
                request.getAddress(), request.getZipCode(), request.getIsDefault());

        return new UserAddressResponse(address);
    }

    // 배송지 삭제
    @Transactional
    public void deleteUserAddress(UUID  addressId) {
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("배송지를 찾을 수 없습니다."));
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        address.delete(deletedBy); // 소프트 삭제 처리
    }
}
