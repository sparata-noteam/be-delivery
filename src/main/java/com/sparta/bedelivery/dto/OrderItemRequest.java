package com.sparta.bedelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private String menuId;
    private int amount;
}
