package com.sparta.bedelivery.dto.store;

import com.sparta.bedelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStoreRequestDto {

    private String userId;

    private String name;

    private String industryName;

    private String locationName;

    private String address;

    private String phone;

    private String imageUrl;

    private Store.Status status;
}
