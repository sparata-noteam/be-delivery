package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(Long userId);

    List<Order> findByStore(String store);
}
