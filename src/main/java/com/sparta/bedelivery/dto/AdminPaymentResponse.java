package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AdminPaymentResponse {
    private final UUID id;
    private final Payment.Status status;
    private final Boolean isDeleted;

    public AdminPaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.status = payment.getStatus();
        this.isDeleted = payment.getDeleteAt() != null;
    }
}
