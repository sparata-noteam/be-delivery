package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentRefundResponse {
    private UUID id;
    private Order.OrderStatus orderStatus;
    private Payment.Status paymentStatus;

    public PaymentRefundResponse(Order.OrderStatus orderStatus, Payment payment) {
        this.id = payment.getId();
        this.orderStatus = orderStatus;
        this.paymentStatus = Payment.Status.REFUNDED;
    }
}
