package com.sparta.bedelivery.entity.repository;

import com.sparta.bedelivery.entity.IndustryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IndustryCategoryRepository extends JpaRepository<IndustryCategory, UUID> {
}
