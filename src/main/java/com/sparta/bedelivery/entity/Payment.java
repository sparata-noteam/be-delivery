package com.sparta.bedelivery.entity;

import com.sparta.bedelivery.dto.CreatePaymentRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PaymentDetail.Method method;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

//    @Column(nullable = false, unique = true, length = 255)
//    private String transactionId;

    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Status status;

    public Payment(User user, Order order) {
        this.order = order;
        this.user = user;
        this.status = Status.PENDING;
    }

    public void refund() {
        this.status = Status.REFUNDED;
    }

    public void cancel() {
        this.status = Status.CANCELLED;
    }

    public enum Status {
        PENDING, PAID, FAILED, CANCELLED, REFUNDED
    }

    public enum Method {
        CREDIT_CARD, KAKAO_PAY, NAVER_PAY, BANK_TRANSFER, POINTS
    }

}

