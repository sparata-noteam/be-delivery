package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;

@Getter
public class CustomerOrderRequest {
    private final String userId;
    private final String storeId;
    private final Order.OrderStatus status;

    public CustomerOrderRequest(String userId, String storeId, Order.OrderStatus status) {
        this.userId = userId;
        this.storeId = storeId;
        this.status = status;
    }
}
