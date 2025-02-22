package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.LocationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationCategoryRepository extends JpaRepository<LocationCategory, UUID> {
    LocationCategory findByName(String name);
}
