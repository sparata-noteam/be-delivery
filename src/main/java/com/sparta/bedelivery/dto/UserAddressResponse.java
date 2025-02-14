package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserAddressResponse {
    private UUID id;
    private String addressName;
    private String recipientName;
    private String address;
    private String zipCode;
    private boolean isDefault;
    private LocalDateTime createdAt;

    // UserAddress 엔티티에서 데이터를 받아 초기화하는 생성자
    public UserAddressResponse(UserAddress address) {
        this.id = address.getId();
        this.addressName = address.getAddressName();
        this.recipientName = address.getRecipientName();
        this.address = address.getAddress();
        this.zipCode = address.getZipCode();
        this.isDefault = address.getIsDefault();
    }
}
