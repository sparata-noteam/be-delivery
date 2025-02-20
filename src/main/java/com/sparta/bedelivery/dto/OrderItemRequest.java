package com.sparta.bedelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemRequest {
    private UUID menuId;
    private int amount;
}
