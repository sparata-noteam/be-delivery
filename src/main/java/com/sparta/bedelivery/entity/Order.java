package com.sparta.bedelivery.entity;

import com.sparta.bedelivery.dto.CreateOrderRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_orders")
public class Order extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String store;


    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderType orderType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime orderedAt;

    public Order() {}

    public Order(CreateOrderRequest createOrderRequest, BigDecimal totalPrice) {
        this.address = createOrderRequest.getAddress();
        this.totalPrice = totalPrice;
        this.store = "248f20b9-6c9b-48e1-ba45-45959c10504e";
        this.status  = OrderStatus.PENDING;
        this.orderType = OrderType.DELIVERY;
        this.orderedAt = LocalDateTime.now();
        this.description = createOrderRequest.getDescription();
    }

    public void who(User user) {
        this.user = user;
    }

    public void confirmOrder() {
        this.status = OrderStatus.CONFIRMED;
        this.orderType = OrderType.TAKEOUT;
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, CANCELLED, DELIVERING, COMPLETED
    }


    public enum OrderType {
        TAKEOUT, DELIVERY
    }
}
