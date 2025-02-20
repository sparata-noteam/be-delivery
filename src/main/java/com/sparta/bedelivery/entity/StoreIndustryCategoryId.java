package com.sparta.bedelivery.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class StoreIndustryCategoryId implements Serializable {
    private UUID store;
    private UUID industryCategory;

}
