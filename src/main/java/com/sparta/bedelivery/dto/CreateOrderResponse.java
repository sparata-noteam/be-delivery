package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@ToString
public class CreateOrderResponse {
    private UUID id;
    private String storeName;
    private Order.OrderStatus status;
    private BigDecimal totalPrice;
    private String description;
    private String message;

    public CreateOrderResponse(Order order, String storeName) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.description = order.getDescription();
        this.message = "주문이 접수되었습니다. 점주가 주문을 확인할 때까지 기다려주세요.";
        this.storeName = storeName;
    }
}
