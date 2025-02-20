package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CustomOrderList {
    private final UUID id;
    private final UUID storeId;
    private final Order.OrderStatus status;
    private final BigDecimal totalPrice;
    private final LocalDateTime orderedAt;

    public CustomOrderList(Order order) {
        this.id = order.getId();
        this.storeId = order.getStore().getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();
    }
}
