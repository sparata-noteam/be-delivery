package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.StoreUpdateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreUpdateRequestRepository extends JpaRepository<StoreUpdateRequest, UUID> {

    List<StoreUpdateRequest> findByStore_Id(UUID storeId);
}
