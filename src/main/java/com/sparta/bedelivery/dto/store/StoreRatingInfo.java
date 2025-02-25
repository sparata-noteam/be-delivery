package com.sparta.bedelivery.dto.store;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreRatingInfo {
    private UUID storeId;
    private Double averageRating;
    private Long reviewCount;

}
