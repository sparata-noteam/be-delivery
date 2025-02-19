package com.sparta.bedelivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {

    private String name;

    @JsonProperty("locationCategoryId")
    private UUID locationCategoryId;

    @JsonProperty("industryCategoryId")
    private UUID industryCategoryId;

    private String address;
    private String phone;
    private String imageUrl;
}
