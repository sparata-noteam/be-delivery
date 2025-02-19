package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.dto.AdminPaymentCondition;
import com.sparta.bedelivery.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentQueryRepository {
    Page<Payment> findAllCondition(Pageable pageable, AdminPaymentCondition condition);
}
