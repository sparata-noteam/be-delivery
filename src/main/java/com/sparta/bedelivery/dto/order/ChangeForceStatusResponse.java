package com.sparta.bedelivery.dto.order;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;

@Getter
public class ChangeForceStatusResponse {
    // 변경 상태
    private final Order.OrderStatus changedStatus;

    public ChangeForceStatusResponse(Order.OrderStatus nextStatus) {
        this.changedStatus = nextStatus;
    }
}
