package com.sparta.bedelivery.entity;

import com.sparta.bedelivery.dto.CreatePaymentRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_payments")
@NoArgsConstructor
public class Payment extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Status status;

    public Payment(CreatePaymentRequest createPaymentRequest, User user, Order order) {
        this.order = order;
        this.amount = createPaymentRequest.getAmount();
        this.user = user;
        this.status = Status.PENDING;
    }

    public enum Status {
        PENDING, PAID, FAILED, REFUNDED
    }
    public enum method {
        KAKAO_PAY, CASH, TOSS, NAVER_PAY, CREDIT_CARD
    }

}

