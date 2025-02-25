package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Store;

import java.util.List;

public interface StoreQueryRepository {
    List<Store> findStoresWithCategory(String industryName, String locationName);
}
