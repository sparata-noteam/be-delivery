package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentCancelResponse {
    private UUID id;
    private Payment.Status status;

    public PaymentCancelResponse(Payment payment) {
        this.id = payment.getId();
        this.status = Payment.Status.CANCELLED;
    }
}
