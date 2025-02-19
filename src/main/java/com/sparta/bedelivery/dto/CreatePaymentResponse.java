package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreatePaymentResponse {
    private UUID id;
    private Payment.Status status;
    private BigDecimal amount;
    private Payment.Method method;
    private BigDecimal reminder;

    public CreatePaymentResponse(Payment payment, BigDecimal reminder) {
        this.id = payment.getId();
        this.status = payment.getStatus();
        this.amount = payment.getAmount();
        this.method = payment.getMethod();
        // 거스름돈
        this.reminder = reminder;

    }
}