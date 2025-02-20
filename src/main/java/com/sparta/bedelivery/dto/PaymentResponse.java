package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Payment;
import com.sparta.bedelivery.entity.PaymentDetail;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;


@Getter
@Setter
public class PaymentResponse {
    private BigDecimal amount;
    private Payment.Method method;
    private Payment.Status status;
    private LocalDateTime paidAt;

    public PaymentResponse(Payment payment) {
        this.amount = payment.getAmount();
        this.method = payment.getMethod();
        this.status = payment.getStatus();
        this.paidAt = payment.getPaidAt();
    }
}
