package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class OwnerOrderRequest {
    private final UUID storeId;
    private final Order.OrderStatus status;
    public OwnerOrderRequest(String storeId, Order.OrderStatus status) {
        this.storeId = UUID.fromString(storeId);
        this.status = status;
    }
}
