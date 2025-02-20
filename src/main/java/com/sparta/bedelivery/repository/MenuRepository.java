package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {
    List<Menu> findByStore_Id(UUID storeId); // store_id 찾아오기
}