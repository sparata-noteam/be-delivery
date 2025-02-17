package com.sparta.bedelivery.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StoreRequestDto {

    private String name;

    private String address;

    private String phone;

    private String imageUrl;

    private UUID industryCategory;
}
