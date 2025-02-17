package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Payment;
import com.sparta.bedelivery.entity.PaymentDetail;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreatePaymentRequest {
    private UUID orderId;
    private BigDecimal amount;
    private PaymentDetail.Method method;
}
