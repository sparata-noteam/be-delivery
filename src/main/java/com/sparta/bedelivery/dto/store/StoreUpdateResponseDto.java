package com.sparta.bedelivery.dto.store;

import com.sparta.bedelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StoreUpdateResponseDto {

    private UUID storeId;
    private Store.Status status;

    public StoreUpdateResponseDto(Store store) {
        this.storeId = store.getId();
        this.status = store.getStatus();
    }
}
