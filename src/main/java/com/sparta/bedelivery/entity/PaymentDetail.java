package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

//@Entity
//@Getter
//@Setter
//@Table(name = "p_payment_details")
public class PaymentDetail {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Method method;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, unique = true, length = 255)
    private String transactionId;

    private LocalDateTime paidAt;

    public enum Method {
        CREDIT_CARD, KAKAO_PAY, NAVER_PAY, BANK_TRANSFER, POINTS
    }
}

