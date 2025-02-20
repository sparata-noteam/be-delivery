package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    List<Store> findByStatus(Store.Status status);
}
