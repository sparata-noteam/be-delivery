package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.dto.order.OrderCalculateSystem;
import com.sparta.bedelivery.dto.order.OrderItemRequest;
import com.sparta.bedelivery.entity.Menu;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class OrderCalculateSystemTest {

    private List<Menu> menuList;
    private List<OrderItemRequest> item;

    OrderCalculateSystem system;

    @BeforeEach
    void setUp() {
        menuList = new ArrayList<>();
        item = new ArrayList<>();
        system = new OrderCalculateSystem(menuList);
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();
        UUID uuid4 = UUID.randomUUID();
        menuList.add(new Menu(uuid1, "menu1", BigDecimal.TEN, "test1", false, null, null, null));
        menuList.add(new Menu(uuid2, "menu2", BigDecimal.valueOf(1000), "test1", false, null, null, null));
        menuList.add(new Menu(uuid3, "menu3", BigDecimal.TEN, "test1", false, null, null, null));
        menuList.add(new Menu(uuid4, "menu4", BigDecimal.valueOf(2000), "test1", false, null, null, null));
        item.add(new OrderItemRequest(uuid1, 3));
        item.add(new OrderItemRequest(uuid2, 3));
        item.add(new OrderItemRequest(uuid3, 3));
        item.add(new OrderItemRequest(uuid4, 3));
    }

    @Test
    void start() {
        system.start(item);
        Assertions.assertThat(system.getTotalPrice()).isEqualTo(BigDecimal.valueOf(9060));
    }
}