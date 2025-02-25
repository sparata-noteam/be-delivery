package com.sparta.bedelivery.dto.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreRequestDto {

    private String name;
//
//    @JsonProperty("locationCategoryId")
//    private UUID locationCategoryId;

    private String industryName;

    private String locationName;

//    @JsonProperty("industryCategoryId")
//    private UUID industryCategoryId;

    private String address;
    private String phone;
    private String imageUrl;
}
