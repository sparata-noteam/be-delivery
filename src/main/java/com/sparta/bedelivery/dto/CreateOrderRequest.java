package com.sparta.bedelivery.dto;


import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderRequest {
     private UUID storeId;
     private Order.OrderType type;
     private List<OrderItemRequest> item;
     private String address;
     private String description;

}
