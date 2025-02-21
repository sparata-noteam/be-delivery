package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    List<Store> findByStatus(Store.Status status);

    @Query("select s from Store s where s.id = :storeId and s.status != 'OPEN'and s.deleteAt is null")
    Optional<Store> findByIdAndDeleteAtIsNullAndOpenStatus(UUID storeId);

}
