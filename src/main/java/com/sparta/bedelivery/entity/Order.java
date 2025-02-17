package com.sparta.bedelivery.entity;

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

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderType orderType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime orderedAt;

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, DELIVERING, COMPLETED
    }

    public enum OrderType {
        TAKEOUT, DELIVERY
    }

    @ManyToOne // 해당 매장을 통해 누구의 매장인지 알 수 있어야 한다. 양방향
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 해당 주문을 어느 매장에서 주문했는지 알 수 있어야 한다. 양방향

    @ManyToOne // 독단적으로 주소를 가져가도 될듯
    @JoinColumn(name = "address_id")
    private UserAddress address;
}
