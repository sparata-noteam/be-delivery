package com.sparta.bedelivery.dto.store;

import com.sparta.bedelivery.dto.menu.MenuResponseDto;
import com.sparta.bedelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class StoreDetailsResponseDto {

    private UUID storeId;
    private String storeName;
    private String storeAddress;
    private String phone;
    private String storeImageUrl;
    private Store.Status status;

    private List<MenuResponseDto> menus;

    public StoreDetailsResponseDto(Store store) {
        this.storeId = store.getId();
        this.storeName = store.getName();
        this.storeAddress = store.getAddress();
        this.phone = store.getPhone();
        this.storeImageUrl = store.getImageUrl();
        this.status = store.getStatus();
        this.menus = store.getMenuList().stream()
                .filter(menu -> !menu.getIsHidden()).map(MenuResponseDto::new).collect(Collectors.toList());
    }
}
