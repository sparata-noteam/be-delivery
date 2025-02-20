package com.sparta.bedelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ToString
public class OrderCalculate {
    private String menuId;
    private String menuName;
    private BigDecimal price;
    private int amount;
}
