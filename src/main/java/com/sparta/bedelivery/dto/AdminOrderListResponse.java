package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminOrderListResponse {
    private final long totalCount;
    private final int current;
    private final List<AdminOrderResponse> orders;

    public AdminOrderListResponse(Page<Order> orders) {
        this.totalCount = orders.getTotalElements();
        this.current = orders.getNumberOfElements();
        this.orders = orders.getContent().stream().map(AdminOrderResponse::new).toList();
    }
}
