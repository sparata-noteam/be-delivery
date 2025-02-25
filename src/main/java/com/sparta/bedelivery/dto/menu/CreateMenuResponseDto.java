package com.sparta.bedelivery.dto.menu;

import com.sparta.bedelivery.entity.Menu;
import com.sparta.bedelivery.entity.MenuImage;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateMenuResponseDto {

    private UUID menuId;
    private UUID storeId;
    private String name;
    private List<String> imageUrl;
    private BigDecimal price;
    private String description;
    private Boolean isHidden;

    public CreateMenuResponseDto(Menu menu) {
        this.menuId = menu.getId();
        this.storeId = menu.getStore().getId();
        this.name = menu.getName();
        this.imageUrl = menu.getImageList().stream().map(MenuImage::getImageUrl).collect(Collectors.toList());
        this.price = menu.getPrice();
        this.description = menu.getDescription();
        this.isHidden = menu.getIsHidden();
    }
}
