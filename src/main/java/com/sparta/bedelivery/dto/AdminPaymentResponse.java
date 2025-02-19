package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;

import java.util.UUID;

@Getter

public class AdminPaymentResponse {
    private final UUID id;
    private final Payment.Status status;

    public AdminPaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.status = payment.getStatus();
    }
}
