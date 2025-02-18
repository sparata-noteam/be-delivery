package com.sparta.bedelivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StoreRequestDto {

    private String name;

    @JsonProperty("locationCategoryId")  // 또는 "location_category_id"
    private UUID locationCategoryId;

    @JsonProperty("industryCategoryId")  // 또는 "industry_category_id"
    private UUID industryCategoryId;

    private String address;
    private String phone;
    private String imageUrl;
}
