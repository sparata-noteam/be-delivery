package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.CreateOrderRequest;
import com.sparta.bedelivery.dto.CreateOrderResponse;
import com.sparta.bedelivery.dto.LoginUser;
import com.sparta.bedelivery.dto.OrderItemRequest;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.OrderRepository;
import com.sparta.bedelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public CreateOrderResponse create(LoginUser loginUser, CreateOrderRequest createOrderRequest) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 계정찾기
        User user = userRepository.findByUserId(loginUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 계정은 존재하지 않습니다."));

        // 가격정보를 가져온뒤에 반영시킨다.
        for (OrderItemRequest orderItemRequest : createOrderRequest.getItem()) {
            BigDecimal price = findItemPrice(orderItemRequest.getMenuId());
            BigDecimal multiply = price.multiply(BigDecimal.valueOf(orderItemRequest.getAmount()));
            totalPrice = totalPrice.add(multiply);
        }

        Order order = new Order(createOrderRequest, totalPrice);
        order.who(user);

        //점주(직원)가 주문을 받는 경우
        if (List.of(User.Role.MANAGER, User.Role.OWNER).contains(loginUser.getRole())) {
            order.confirmOrder();
        }


        Order orderResponse = orderRepository.save(order);
        return new CreateOrderResponse(orderResponse, "대충 아무거나");
    }

    private BigDecimal findItemPrice(String menuId) {
        // 대충 검색하고
        return BigDecimal.valueOf(2000);
    }
}
