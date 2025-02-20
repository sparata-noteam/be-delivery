package com.sparta.bedelivery.entity;

import com.sparta.bedelivery.dto.CreateOrderRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Column(nullable = false, length = 255)
    private String userId;

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


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_Id")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(CreateOrderRequest createOrderRequest, BigDecimal totalPrice) {
        this.address = createOrderRequest.getAddress();
        this.totalPrice = totalPrice;
        this.store = String.valueOf(createOrderRequest.getStoreId());
        this.status = OrderStatus.PENDING;
        this.orderType = OrderType.DELIVERY;
        this.orderedAt = LocalDateTime.now();
        this.description = createOrderRequest.getDescription();
    }

    public void who(String user) {
        this.userId = user;
    }

    public void confirmOrder() {
        this.status = OrderStatus.CONFIRMED;
        this.orderType = OrderType.TAKEOUT;
    }

    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    public void addMenu(List<OrderItem> prepareOrderItems) {
        this.orderItems.addAll(prepareOrderItems);
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, CANCELLED, DELIVERING, COMPLETED
    }


    public enum OrderType {
        TAKEOUT, DELIVERY
    }
}
