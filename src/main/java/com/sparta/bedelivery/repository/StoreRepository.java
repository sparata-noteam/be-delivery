package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    List<Store> findByStatus(Store.Status status);

    //삭제되지 않은 모든 매장 조회
    List<Store> findAllByDeleteAtIsNull();
}
