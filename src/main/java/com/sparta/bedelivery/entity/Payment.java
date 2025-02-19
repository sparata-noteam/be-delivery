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


    @Column(length = 255)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Payment.Method method;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

//    @Column(nullable = false, unique = true, length = 255)
//    private String transactionId;

    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Status status;

    public Payment(Order order) {
        this.order = order;
        this.status = Status.PENDING;
    }

    public void refund() {
        this.status = Status.REFUNDED_CALL;
    }

    public void cancel() {
        this.status = Status.PENDING;
    }

    public void checkAmount(BigDecimal totalPrice) {
        BigDecimal buyAmount = this.getAmount();
        if (buyAmount.compareTo(totalPrice) < 0) {
            throw new IllegalArgumentException("주문 금액보다 낮은 금액으로 결제가 불가능합니다.");
        }

    }

    // 결제 상태 변경, 결제 수단 등록, 결제 시작!
    public void start(String userId, CreatePaymentRequest createPaymentRequest) {
        this.userId = userId;
        this.method = createPaymentRequest.getMethod();
        this.amount = createPaymentRequest.getAmount();
        this.status = Status.PAID;
    }

    public void refundDone() {
        this.status = Status.REFUNDED;
    }

    // 초기화
    public void initStatus() {
        this.status = Status.PENDING;
        this.userId = null;
        this.method = null;
        this.amount = BigDecimal.ZERO;
    }

    //실패시 결재 대기로 돌아간다.
    public enum Status {
        PENDING, PAID, REFUNDED_CALL, REFUNDED
    }

    public enum Method {
        CREDIT_CARD, KAKAO_PAY, NAVER_PAY, BANK_TRANSFER, POINTS
    }

}

