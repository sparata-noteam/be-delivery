package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.IndustryCategory;
import com.sparta.bedelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class StoreResponseDto {

    private UUID storeId;
    private String name;
    private String address;
    private String phone;
    private String image_url;
    private Store.Status status;
    private List<String> industryCategoryNames; // 수정: List<String>으로 변경

    // 전체 매장 목록 조회 (사용자용)
    public StoreResponseDto(Store store) {
        this.storeId = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.image_url = store.getImageUrl();
        this.industryCategoryNames = store.getStoreIndustryCategories().stream()
                .map(storeIndustryCategory -> storeIndustryCategory.getIndustryCategory().getName())
                .collect(Collectors.toList());
        this.status = store.getStatus();
    }
}

