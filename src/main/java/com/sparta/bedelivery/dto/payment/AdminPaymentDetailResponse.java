package com.sparta.bedelivery.dto.payment;

import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class AdminPaymentDetailResponse {
    private final UUID paymentId;
    private final Payment.Status status;
    private final BigDecimal amount;

    public AdminPaymentDetailResponse(Payment payment) {
        this.paymentId = payment.getId();
        this.status = payment.getStatus();
        this.amount = payment.getAmount();
    }
}
