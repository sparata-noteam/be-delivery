package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_order_items")
public class OrderItem extends BaseSystemFieldEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne // 어떤 사용자가 주문을 한 건지 알아야 한다.
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
}

