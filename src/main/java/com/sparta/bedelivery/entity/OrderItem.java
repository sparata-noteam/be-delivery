package com.sparta.bedelivery.entity;

import com.sparta.bedelivery.dto.OrderCalculate;
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
@Table(name = "p_order_items")
@NoArgsConstructor
public class OrderItem extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private String menuId;

    @Column
    private String menuName;

    public OrderItem(OrderCalculate orderCalculate) {
        this.quantity = orderCalculate.getAmount();
        this.menuId = orderCalculate.getMenuId();
        this.menuName = orderCalculate.getMenuName();
        this.price = orderCalculate.getPrice();
    }


}

