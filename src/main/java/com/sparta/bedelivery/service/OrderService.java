package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.*;
import com.sparta.bedelivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;


    @Transactional
    public CreateOrderResponse create(LoginUser loginUser, CreateOrderRequest createOrderRequest) {
        // 계정찾기
        User user = userRepository.findByUserId(loginUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 계정은 존재하지 않습니다."));

        // 해당하는 메뉴를 찾는다.
        List<OrderItemRequest> items = createOrderRequest.getItem();
        List<UUID> allMenuIdList = items.stream().map(OrderItemRequest::getMenuId).toList();
        
        List<Menu> allMenuList = menuRepository.findAllById(allMenuIdList);

        OrderCalculateSystem calculate = new OrderCalculateSystem(allMenuList);
        List<OrderCalculate> calculates = calculate.start(items);

        Order order = new Order(createOrderRequest, calculate.getTotalPrice());
        Store store = storeRepository.findById(createOrderRequest.getStoreId()).orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
        order.addStore(store);
        order.who(user.getUserId());

        order.addMenu(calculates.stream().map(OrderItem::new).toList());

        //점주(직원)가 주문을 받는 경우
        if (List.of(User.Role.MANAGER, User.Role.OWNER).contains(loginUser.getRole())) {
            order.confirmOrder();
        }

        Order orderResponse = orderRepository.save(order);

        paymentRepository.save(new Payment(order));

        return new CreateOrderResponse(orderResponse, store.getName());
    }


    public CustomerOrderResponse getCustomerOrderList(Pageable pageable, CustomerOrderRequest condition) {
        User user = userRepository.findByUserId(condition.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당하는 계정이 존재하지 않습니다."));
        Page<Order> allUsers = orderRepository.findAllOrdersByCustomer(pageable, condition);
        return new CustomerOrderResponse(allUsers);
    }

    public OwnerOrderListResponse getOwnerOrderList(Pageable pageable, OwnerOrderRequest condition) {
        Page<Order> allOrderForOwnerList = orderRepository.findAllOwner(pageable, condition);
        return new OwnerOrderListResponse(allOrderForOwnerList);
    }


    public OrderDetailResponse getDetails(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다.")
        );

        Payment payment = paymentRepository.findByOrderId(UUID.fromString(orderId)).orElseThrow(() ->
                new IllegalArgumentException("해당하는 결제가 존재하지 않습니다."));

        return new OrderDetailResponse(order, payment);
    }

    @Transactional
    public OrderAcceptResponse accept(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 결재가 존재하지 않습니다."));

        Order.OrderStatus status = order.getStatus();
        //주문 확인이 완료된 경우
        if (status == Order.OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("주문 확인이 완료되었습니다.");
        }

        //취소가된 경우
        if (status == Order.OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("취소된 주문입니다.");
        }

        // 결제 대기 상태가 아닌경우
        if (payment.getStatus() != Payment.Status.PAID) {
            throw new IllegalArgumentException("결제 대기인 상태인 주문만 취소가 가능합니다.");
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
    //결제 진행중인 경우에만 사용가능 -> 배달 진행, 배달 완료만 표기가능
    public OrderStatusResponse status(UUID orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 결제가 존재하지 않습니다."));

        if (payment.getStatus() != Payment.Status.PAID) {
            throw new IllegalArgumentException("주문 상태는 결제 진행중인 경우에만 변경이 가능합니다.");
        }

        order.changeStatus(status);

        return new OrderStatusResponse(order);
    }

    @Transactional
    public OrderCancelResponse cancel(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));
        LocalDateTime orderTime = order.getOrderedAt().plusMinutes(5L);
        LocalDateTime now = LocalDateTime.now();
        Order.OrderStatus status = order.getStatus();

        if (status != Order.OrderStatus.PENDING) {
            throw new IllegalArgumentException("주문은 대기 상태일때만 취소할 수 있습니다.");
        }

        if (now.isAfter(orderTime)) {
            throw new IllegalArgumentException("주문 생성후 5분이내에만 취소가 가능합니다.");
        }

        // 주문 취소
        order.cancel();

        // 아이템 제거

        return new OrderCancelResponse(order);
    }

    @Transactional
    public ChangeForceStatusResponse forceChange(UUID orderId, Order.OrderStatus nextStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문은 존재하지 않습니다."));

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 결제는 존재하지 않습니다."));

        Order.OrderStatus currentStatus = order.getStatus();

        // 주문상태는 초기화가 가능합니다.
        // 다만 결제 상태가 발생한 이후에는 배달과 배달 처리만 가능합니다.
        LinkedList<Order.OrderStatus> orderStatusLinkedList = new LinkedList<>();
        // 결제 이전에 바꿀 수 있음
        orderStatusLinkedList.add(0, Order.OrderStatus.PENDING);
        orderStatusLinkedList.add(1, Order.OrderStatus.CONFIRMED);
        // 결제 이후 진행이 되어야 함
        orderStatusLinkedList.add(2, Order.OrderStatus.DELIVERING);
        orderStatusLinkedList.add(3, Order.OrderStatus.COMPLETED);


        Payment.Status currentPaymentStatus = payment.getStatus();
        // 결제 대기상태 && 주문 대기, 주문확인으로 바꾸는게 가능합니다.
        if (currentPaymentStatus == Payment.Status.PENDING && orderStatusLinkedList.indexOf(currentStatus) < 2) {
            order.changeStatus(nextStatus);
            return new ChangeForceStatusResponse(nextStatus);
        }

        // 결제 상태 && 배달중, 완료상태인 경우에만 변경할 수 있습니다.
        if (currentPaymentStatus == Payment.Status.PAID && orderStatusLinkedList.indexOf(currentStatus) > 1) {
            order.changeStatus(nextStatus);
            return new ChangeForceStatusResponse(nextStatus);
        }

        // 결제 상태가 진행중인데 주문대기로 초기화하는 경우
        // TODO: 실제 환불처리는 일어나지 않습니다.
        if (currentPaymentStatus == Payment.Status.PENDING && orderStatusLinkedList.indexOf(currentStatus) > 1) {
            order.changeStatus(nextStatus);
            payment.initStatus();
            return new ChangeForceStatusResponse(nextStatus);
        }

        //그 이외의 조건은 주문 상태값은 비즈니스 플로우가 적용되어집니다.
        if (orderStatusLinkedList.indexOf(nextStatus) - orderStatusLinkedList.indexOf(currentStatus) != 1) {
            throw new IllegalArgumentException("변경할 수 없는 주문 상태입니다. 주문을 다시 확인해주세요.");
        }

        return new ChangeForceStatusResponse(nextStatus);
    }

    @Transactional
    public Object deleteOrder(LoginUser loginUser, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문은 존재하지 않습니다."));

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 결제는 존재하지 않습니다."));

        // 결제 상태인 경우에 주문을 삭제하는 경우에는 결제 상태 초기화
        //TODO 실제 환불 처리는 일어나지 않습니다.
        if (payment.getStatus() == Payment.Status.PAID) {
            payment.initStatus();
        }

        order.delete(loginUser.getUserId());
        return null;
    }

    public AdminOrderListResponse getOrderList(Pageable pageable, AdminOrderCondition condition) {
        Page<Order> orders = orderRepository.findByAdmin(pageable, condition);
        return new AdminOrderListResponse(orders);
    }
}
