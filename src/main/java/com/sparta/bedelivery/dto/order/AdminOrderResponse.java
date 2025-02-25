package com.sparta.bedelivery.dto.order;

import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AdminOrderResponse {
    private final UUID id;
    private final UUID storeId;
    private final Order.OrderStatus status;
    private final BigDecimal totalPrice;
    private final LocalDateTime orderedAt;
    private final Boolean isDeleted;

    public AdminOrderResponse(Order order) {
        this.id = order.getId();
        this.storeId = order.getStore().getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();
        this.isDeleted = order.getDeleteAt() != null;
    }

}
