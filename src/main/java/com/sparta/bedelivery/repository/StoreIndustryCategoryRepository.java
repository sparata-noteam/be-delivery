package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.StoreIndustryCategory;
import com.sparta.bedelivery.entity.StoreIndustryCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreIndustryCategoryRepository extends JpaRepository<StoreIndustryCategory, StoreIndustryCategoryId> {
}
