package com.sparta.bedelivery.dto.store;

import com.sparta.bedelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateStoreResponseDto {

    private String userId;

    private UUID storeId;

    private Store.Status status;

    public CreateStoreResponseDto(Store store) {
        this.userId = store.getUser().getUserId();
        this.storeId = store.getId();
        this.status = store.getStatus();
    }
}
