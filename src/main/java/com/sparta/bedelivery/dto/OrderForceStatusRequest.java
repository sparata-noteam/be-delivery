package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;

@Getter
// 강제로 주문 상태를 바꿔준다.
public class OrderForceStatusRequest {
    private Order.OrderStatus status;

}
