package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderQueryRepository {
//    List<Order> findByUserId(String userId);
//
//    List<Order> findByStore(Store storeId);
}
