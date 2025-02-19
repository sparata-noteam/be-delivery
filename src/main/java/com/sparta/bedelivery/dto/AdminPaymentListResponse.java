package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AdminPaymentListResponse {
    private final long totalCount;
    private final int current;
    private final List<AdminPaymentResponse> payments;

    public AdminPaymentListResponse(Page<Payment> payments) {
        this.totalCount = payments.getTotalElements();
        this.current = payments.getNumberOfElements();
        this.payments = payments.stream().map(AdminPaymentResponse::new).toList();
    }
}

