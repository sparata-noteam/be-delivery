package com.sparta.bedelivery.dto.order;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.Setter;

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
