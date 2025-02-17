package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.Menu;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.OrderItem;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.MenuRepository;
import com.sparta.bedelivery.repository.OrderRepository;
import com.sparta.bedelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public CreateOrderResponse create(LoginUser loginUser, CreateOrderRequest createOrderRequest) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 계정찾기
        User user = userRepository.findByUserId(loginUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 계정은 존재하지 않습니다."));

        // 해당하는 메뉴를 찾는다.
        List<OrderItemRequest> items = createOrderRequest.getItem();
        List<UUID> allMenuIdList = items.stream().map(OrderItemRequest::getMenuId).toList();
        List<Menu> allMenuList = menuRepository.findAllById(allMenuIdList);

        List<OrderCalculate> calculates = new ArrayList<>();

        // 가격정보를 가져온뒤에 반영시킨다.
        // 히든 처리된 상품은 가져오지 않는다.
        for (OrderItemRequest orderItemRequest : createOrderRequest.getItem()) {
            Menu menu = allMenuList.stream().filter(m ->
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

        Order order = new Order(createOrderRequest, totalPrice);
        order.who(user);

        order.addMenu(calculates.stream().map(OrderItem::new).toList());

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


    public List<OwnerOrderResponse> getOwnerOrderList(String storeId) {
        List<Order> orders = orderRepository.findByStore(storeId);
        return orders.stream().map(OwnerOrderResponse::new).toList();
    }


    public OrderDetailResponse getDetails(String orderId) {

        Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다.")
        );

        //TODO: 결재 정보를 가져온다.

        return new OrderDetailResponse(order);
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

    @Transactional
    public OrderCancelResponse cancel(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));
        LocalDateTime orderTime = order.getOrderedAt().plusMinutes(5L);
        LocalDateTime now = LocalDateTime.now();
        Order.OrderStatus status = order.getStatus();

        if(status != Order.OrderStatus.PENDING) {
            throw new IllegalArgumentException("주문은 대기 상태일때만 취소할 수 있습니다.");
        }

        if(now.isAfter(orderTime)) {
            throw new IllegalArgumentException("주문 생성후 5분이내에만 취소가 가능합니다.");
        }

        // 주문 취소
        order.cancel();

        // 아이템 제거

        return new OrderCancelResponse(order);
    }
}
