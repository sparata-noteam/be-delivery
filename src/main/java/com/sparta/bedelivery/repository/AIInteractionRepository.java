package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.AIInteraction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIInteractionRepository extends JpaRepository<AIInteraction, UUID> {
}
