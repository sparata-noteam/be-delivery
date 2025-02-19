package com.sparta.bedelivery.dto;


import com.sparta.bedelivery.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CustomerOrderResponse {


    private List<CustomOrderList> orders;
    private long totalCount;
    private int current;

    public CustomerOrderResponse(Page<Order> orderPage) {
        this.totalCount = orderPage.getTotalElements();
        this.current = orderPage.getNumberOfElements();
        this.orders = orderPage.getContent().stream().map(or ->  new CustomOrderList(or)).toList();

    }
}
