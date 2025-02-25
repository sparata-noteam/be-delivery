package com.sparta.bedelivery.dto.order;

import com.sparta.bedelivery.entity.Menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class OrderCalculateSystem {
    private final List<Menu> menuList;
    private BigDecimal totalPrice;

    public OrderCalculateSystem(List<Menu> menuList) {
        this.menuList = menuList;
        totalPrice = BigDecimal.ZERO;
    }

    public List<OrderCalculate> start(List<OrderItemRequest> items) {
        List<OrderCalculate> calculates = new ArrayList<>();
        // 가격정보를 가져온뒤에 반영시킨다.
        // 히든 처리된 상품은 가져오지 않는다.
        for (OrderItemRequest orderItemRequest : items) {
            Menu menu = menuList.stream().filter(m ->
                            m.getId().equals(orderItemRequest.getMenuId()))
                    .filter(m -> !m.getIsHidden())
                    .findFirst().orElse(null);

            // 메뉴가 존재하지 않는 경우에는 무시한다.
            if (menu == null) continue;

            BigDecimal multiply = menu.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getAmount()));

            // 메뉴를 넣는다.
            calculates.add(new OrderCalculate(menu.getId().toString(),
                    menu.getName(),
                    multiply,
                    orderItemRequest.getAmount()));
            totalPrice = totalPrice.add(multiply);
        }
        return calculates;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
