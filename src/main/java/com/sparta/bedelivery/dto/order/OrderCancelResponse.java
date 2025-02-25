package com.sparta.bedelivery.dto.order;

import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class OrderCancelResponse {
    private UUID id;
    private Order.OrderStatus orderStatus;
    private Payment.Status paymentStatus;

    public OrderCancelResponse(Order order) {
        this.id = order.getId();
        this.orderStatus = Order.OrderStatus.CANCELLED;
        this.paymentStatus = null;
    }
}
