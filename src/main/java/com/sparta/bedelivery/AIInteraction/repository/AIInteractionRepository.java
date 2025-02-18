package com.sparta.bedelivery.AIInteraction.repository;

import com.sparta.bedelivery.AIInteraction.entity.AIInteraction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIInteractionRepository extends JpaRepository<AIInteraction, UUID> {
}
