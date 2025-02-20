package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Order;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class OwnerOrderListResponse {
    private final long totalCount;
    private final int current;
    private final List<OwnerOrderResponse> orders;

    public OwnerOrderListResponse(Page<Order> orders) {
        this.totalCount = orders.getTotalElements();
        this.current = orders.getNumberOfElements();
        this.orders = orders.stream().map(OwnerOrderResponse::new).toList();
    }
}
