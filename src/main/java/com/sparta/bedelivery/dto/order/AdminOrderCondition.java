package com.sparta.bedelivery.dto.order;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AdminOrderCondition {
    private final String storeId;
    private final Order.OrderStatus status;
    private final Boolean isDeleted;

    public AdminOrderCondition(Order.OrderStatus status, String storeId, Boolean isDeleted) {
        this.storeId = storeId;
        this.status = status;
        this.isDeleted = isDeleted;
    }
}
