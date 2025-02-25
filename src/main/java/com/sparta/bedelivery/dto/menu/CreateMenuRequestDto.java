package com.sparta.bedelivery.dto.menu;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateMenuRequestDto {

//    private UUID storeId;
    private String storeAddress;
    private String name;
    private BigDecimal price;
    private List<String> imageUrl;
    private String description;
    private Boolean isHidden;
}
