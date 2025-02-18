package com.sparta.bedelivery.AIInteraction.repository;

import com.sparta.bedelivery.entity.Store;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, UUID> {
}
