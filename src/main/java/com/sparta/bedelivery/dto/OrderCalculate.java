package com.sparta.bedelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderCalculate {
    private String menuId;
    private String menuName;
    private BigDecimal price;
    private int amount;
}
