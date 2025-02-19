package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.IndustryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IndustryCategoryRepository extends JpaRepository<IndustryCategory, UUID> {
}
