package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.OrderRepository;
import com.sparta.bedelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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


    public List<CustomerOrderResponse> getCustomerOrderList(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("해당하는 계정이 존재하지 않습니다."));
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orders.stream().map(CustomerOrderResponse::new).toList();
    }

    private BigDecimal findItemPrice(String menuId) {
        // 대충 검색하고
        return BigDecimal.valueOf(2000);
    }

    public List<OwnerOrderResponse> getOwnerOrderList(String storeId) {
        List<Order> orders = orderRepository.findByStore(storeId);
        return orders.stream().map(OwnerOrderResponse::new).toList();
    }

    @Transactional
    public OrderAcceptResponse accept(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));
        Order.OrderStatus status = order.getStatus();
        //주문 확인이 완료된 경우
        if (status == Order.OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("주문 확인이 완료되었습니다.");
        }
        //취소가된 경우
        if (status == Order.OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("취소된 주문입니다.");
        }
        //배달중인 경우
        if (status == Order.OrderStatus.DELIVERING) {
            throw new IllegalArgumentException("이미 배달 중인 주문입니다.");
        }
        //배달이 완료된 경우
        if (status == Order.OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("배달이 완료된 주문입니다.");
        }

        //완료처리
        order.confirm();

        return new OrderAcceptResponse(order);
    }

    @Transactional
    public OrderStatusResponse status(String orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));

        order.changeStatus(status);

        return new OrderStatusResponse(order);
    }
}
