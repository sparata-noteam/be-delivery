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
// 특정 매장 상세 조회를 위한 메뉴 및 리뷰 dto
public class MenuResponseDto {

    private UUID menuId;
    private String name;
    private BigDecimal price;
    private String description;
    private List<String> imageUrl;
    private Boolean isHidden;
//    private List<ReviewResponseDto> reviews;

    public MenuResponseDto(Menu menu) {
        this.menuId = menu.getId();
        this.name = menu.getName();
        this.imageUrl = menu.getImageList().stream().map(MenuImage::getImageUrl).collect(Collectors.toList());
        this.price = menu.getPrice();
        this.description = menu.getDescription();
        this.isHidden = menu.getIsHidden();
//        this.reviews = menu.getReviewList()
//                .stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }
}
