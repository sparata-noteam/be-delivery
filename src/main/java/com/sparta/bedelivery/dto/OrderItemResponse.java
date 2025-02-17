package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class OrderItemResponse {
    private String menuId;
    private String menuName;
    private int quantity;
    private BigDecimal price;

    public OrderItemResponse(OrderItem item) {
        this.menuId = item.getMenuId();
        this.menuName = item.getMenuName();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
    }
}
