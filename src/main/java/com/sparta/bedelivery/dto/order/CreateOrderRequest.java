package com.sparta.bedelivery.dto.order;


import com.sparta.bedelivery.entity.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateOrderRequest {
     private UUID storeId;
     private Order.OrderType type;
     private List<OrderItemRequest> item;
     private String address;
     private String description;

}
