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
    private String method;

    public CreatePaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.status = payment.getStatus();
//        this.amount = payment.getAmount();
        //TODO 메소드 입력
    }
}