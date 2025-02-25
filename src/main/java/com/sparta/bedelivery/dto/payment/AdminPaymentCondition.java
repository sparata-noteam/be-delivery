package com.sparta.bedelivery.dto.payment;

import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;

@Getter

public class AdminPaymentCondition {
    private final Payment.Status status;
    private final Payment.Method method;
    private final Boolean isDeleted;
    public AdminPaymentCondition(Payment.Status status, Payment.Method method, Boolean isDeleted) {
        this.status = status;
        this.method = method;
        this.isDeleted = isDeleted;
    }
}
