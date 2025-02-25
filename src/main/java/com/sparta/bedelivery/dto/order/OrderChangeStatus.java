package com.sparta.bedelivery.dto.order;

import com.sparta.bedelivery.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderChangeStatus {
    private Order.OrderStatus status;
}
