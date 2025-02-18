package com.sparta.bedelivery.dto;


import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CustomerOrderResponse {
    private UUID id;
    private String storeId;
    private Order.OrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime orderedAt;

    public CustomerOrderResponse(Order order) {
        this.id = order.getId();
        this.storeId = order.getStore();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();

    }
}
