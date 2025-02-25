package com.sparta.bedelivery.dto.order;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderStatusResponse {
    private UUID orderId;
    private Order.OrderStatus status;
    private Order.OrderType orderType;
    private String message;

    public OrderStatusResponse(Order order) {
        this.orderId = order.getId();
        this.status = order.getStatus();
        this.orderType = order.getOrderType();

        if (orderType == Order.OrderType.DELIVERY) {
            this.message = "주문이 배달 중입니다.";
        }
        if (orderType == Order.OrderType.TAKEOUT) {
            this.message = "포장주문이 완료되었습니다";
        }

    }
}
