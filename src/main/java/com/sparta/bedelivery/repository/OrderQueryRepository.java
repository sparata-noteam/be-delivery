package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.dto.AdminOrderCondition;
import com.sparta.bedelivery.dto.CustomerOrderRequest;
import com.sparta.bedelivery.dto.CustomerOrderResponse;
import com.sparta.bedelivery.dto.OwnerOrderRequest;
import com.sparta.bedelivery.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderQueryRepository {

    Page<Order> findAllUsers(Pageable pageable, CustomerOrderRequest condition);

    Page<Order> findAllOwner(Pageable pageable, OwnerOrderRequest condition);

    Page<Order> findByAdmin(Pageable pageable, AdminOrderCondition condition);
}
