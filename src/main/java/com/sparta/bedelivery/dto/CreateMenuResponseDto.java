package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Menu;
import com.sparta.bedelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateMenuResponseDto {

    private UUID menuId;
    private UUID storeId;
    private String name;
    private BigDecimal price;
    private String description;
    private Boolean isHidden;

    public CreateMenuResponseDto(Menu menu) {
        this.menuId = menu.getId();
        this.storeId = menu.getStore().getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.description = menu.getDescription();
        this.isHidden = menu.getIsHidden();
    }
}
