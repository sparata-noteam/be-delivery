package com.sparta.bedelivery.dto.store;

import com.sparta.bedelivery.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class StoreStatusResponseDto {

    private UUID storeId;
    private String storeName;
    private Store.Status status;

    public StoreStatusResponseDto(Store status) {
        this.storeId = status.getId();
        this.storeName = status.getName();
        this.status = status.getStatus();
    }
}
