package com.sparta.bedelivery.AIInteraction.repository;

import com.sparta.bedelivery.AIInteraction.entity.AIInteraction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AIInteractionRepository extends JpaRepository<AIInteraction, UUID> {
}
