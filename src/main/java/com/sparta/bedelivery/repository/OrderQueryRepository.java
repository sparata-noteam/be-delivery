package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.dto.AdminOrderCondition;
import com.sparta.bedelivery.dto.CustomerOrderRequest;
import com.sparta.bedelivery.dto.OwnerOrderRequest;
import com.sparta.bedelivery.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepository {

    Page<Order> findAllOrdersByCustomer(Pageable pageable, CustomerOrderRequest condition);

    Page<Order> findAllOwner(Pageable pageable, OwnerOrderRequest condition);

    Page<Order> findByAdmin(Pageable pageable, AdminOrderCondition condition);
}
