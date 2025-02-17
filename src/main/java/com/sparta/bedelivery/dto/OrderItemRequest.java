package com.sparta.bedelivery.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderItemRequest {
    private UUID menuId;
    private int amount;
}
