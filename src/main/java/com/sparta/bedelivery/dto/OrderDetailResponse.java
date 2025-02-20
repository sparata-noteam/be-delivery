package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderDetailResponse {
    private final UUID id;
    private final String userId;
    private final UUID storeId;
    private final String storeName;
    private final String userName;
    private final Order.OrderStatus status;
    private final BigDecimal totalPrice;
    private final String description;
    private final LocalDateTime orderedAt;
    private final List<OrderItemResponse> items;
    private final PaymentResponse payment;

    public OrderDetailResponse(Order order, Payment payment) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.storeId = order.getStore().getId();
        this.storeName = order.getStore().getName();
        this.userName = order.getUserId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.description = order.getDescription();
        this.orderedAt = order.getOrderedAt();
        this.items = order.getOrderItems().stream().map(OrderItemResponse::new).toList();
        this.payment = new PaymentResponse(payment);
    }
}
