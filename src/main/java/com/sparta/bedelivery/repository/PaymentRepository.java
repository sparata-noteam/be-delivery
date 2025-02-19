package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>, PaymentQueryRepository {
    Optional<Payment> findByOrderId(UUID orderId);
}
