package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRefundResponse {
       private String id;
       private Order.OrderStatus orderStatus;
       private Payment.Status paymentStatus;
}
