package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderDetailResponse {
    private UUID id;
    private String userId;
    private String storeId;
    private String storeName;
    private String userName;
    private Order.OrderStatus status;
    private BigDecimal totalPrice;
    private String description;
    private LocalDateTime orderedAt;
    private List<OrderItemResponse> items;
    private PaymentResponse payment;

    public OrderDetailResponse(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getUserId();
        this.storeId = order.getStore();
        this.storeName = "아무거나";
        this.userName = order.getUser().getName();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.description = order.getDescription();
        this.orderedAt = order.getOrderedAt();

        this.items = order.getOrderItems().stream().map(it -> new OrderItemResponse(it)).toList();
        //작업전
        this.payment = null;
    }
}
