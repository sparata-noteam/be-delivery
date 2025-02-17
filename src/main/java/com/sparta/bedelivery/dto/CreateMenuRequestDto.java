package com.sparta.bedelivery.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateMenuRequestDto {

    private UUID storeId;
    private String name;
    private BigDecimal price;
    private String description;
    private Boolean isHidden;
}
