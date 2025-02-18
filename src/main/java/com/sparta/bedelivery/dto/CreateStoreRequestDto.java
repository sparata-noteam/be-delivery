package com.sparta.bedelivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.bedelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateStoreRequestDto {

    private String userId;

    private String name;

    @JsonProperty("locationCategoryId")  // 또는 "location_category_id"
    private UUID locationCategoryId;

    @JsonProperty("industryCategoryId")  // 또는 "industry_category_id"
    private UUID industryCategoryId;

    private String address;

    private String phone;

    private String imageUrl;

    private Store.Status status;
}
