package com.sparta.bedelivery.entity.repository;

import com.sparta.bedelivery.entity.StoreIndustryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreIndustryCategoryRepository extends JpaRepository<StoreIndustryCategory, UUID> {
}
