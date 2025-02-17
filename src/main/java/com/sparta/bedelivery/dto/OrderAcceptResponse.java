package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderAcceptResponse {
    private UUID orderId;
    private Order.OrderStatus status;
    private Order.OrderType orderType;
    private String message;

    public OrderAcceptResponse(Order order) {
        this.orderId = order.getId();
        this.status = order.getStatus();
        this.orderType = order.getOrderType();
        this.message = "주문이 접수되었습니다. 조리가 시작됩니다";
    }
}
