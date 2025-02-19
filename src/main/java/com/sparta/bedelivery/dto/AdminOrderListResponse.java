package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
public class AdminOrderListResponse {
    private final UUID orderId;
    private final Order.OrderStatus status;

    public AdminOrderListResponse(Order order) {
        this.orderId = order.getId();
        this.status = order.getStatus();
    }
}
