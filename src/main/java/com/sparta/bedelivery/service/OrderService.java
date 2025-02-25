package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.order.*;
import com.sparta.bedelivery.dto.user.LoginUser;
import com.sparta.bedelivery.entity.*;
import com.sparta.bedelivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

        UUID storeId = createOrderRequest.getStoreId();
        Optional<Store> notOpenStore = storeRepository.findByIdAndDeleteAtIsNullAndOpenStatus(storeId);

        if(notOpenStore.isPresent()) {
            throw new IllegalArgumentException("가게는 오픈된 상태만 주문을 넣을 수 있습니다.");
        }

        List<Menu> allMenuList = menuRepository.findAllById(allMenuIdList);

        OrderCalculateSystem calculate = new OrderCalculateSystem(allMenuList);
        List<OrderCalculate> calculates = calculate.start(items);

        Order order = new Order(createOrderRequest, calculate.getTotalPrice());
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
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
        if (payment.getStatus() != Payment.Status.PENDING) {
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
    public OrderCancelResponse cancel(LoginUser loginUser, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해단하는 결제는 존재하지 않습니다."));
        LocalDateTime orderTime = order.getOrderedAt().plusMinutes(5L);
        LocalDateTime now = LocalDateTime.now();

        // 고객은 결제대기 상태에서만 취소할수 있습니다.
        if (loginUser.getRole() == User.Role.CUSTOMER &&
            payment.getStatus() != Payment.Status.PENDING) {
            throw new IllegalArgumentException("주문은 결제 대기 상태일때만 취소할 수 있습니다.");
        }

        if (now.isAfter(orderTime)) {
            throw new IllegalArgumentException("주문 생성후 5분이내에만 취소가 가능합니다.");
        }

        //가게주인 or 직원이 주문 취소하는 경우
        //환불처리한다.
        if (List.of(User.Role.OWNER, User.Role.MANAGER).contains(loginUser.getRole())) {
            payment.initStatus();
        }

        // 주문 취소
        order.cancel();

        // 아이템 제거
        return new OrderCancelResponse(order);
    }

    @Transactional
    public ChangeForceStatusResponse forceChange(UUID orderId, Order.OrderStatus nextStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 주문은 존재하지 않습니다."));

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결제는 존재하지 않습니다."));

        List<Order.OrderStatus> orderStatusList = List.of(
                Order.OrderStatus.PENDING,
                Order.OrderStatus.CONFIRMED,
                Order.OrderStatus.DELIVERING,
                Order.OrderStatus.COMPLETED);

        Order.OrderStatus orderStatus = order.getStatus();
        Payment.Status paymentStatus = payment.getStatus();

        // 주문취소로 변경하고 싶은 경우
        // 결제 상태가 아닐때
        if (payment.getStatus() == Payment.Status.PENDING && nextStatus == Order.OrderStatus.CANCELLED) {
            order.changeStatus(nextStatus);
            return new ChangeForceStatusResponse(nextStatus);
        }

        // 결제 상태일때 취소가 되어지는 경우
        // 환불처리를 한다.
        if (payment.getStatus() == Payment.Status.PAID && nextStatus == Order.OrderStatus.CANCELLED) {
            order.changeStatus(nextStatus);
            payment.initStatus();
            return new ChangeForceStatusResponse(nextStatus);
        }

        // 결제 완료 상태에서 강제적으로 PENDING이나 CONFIRMED로 변경하려는 경우
        // 관리자 API이므로 강제로 처리하지만, 그로 인한 부작용을 관리해야 한다.
        if (paymentStatus == Payment.Status.PAID && orderStatusList.indexOf(nextStatus) < 2) {
            payment.initStatus();  // 결제 상태 초기화
            // 추가적으로 환불 처리 로직이 필요하다면 여기서 처리
        }

        // 결제 상태가 팬딩 상태이고 배달중인 경우에는 강제로 변경할 수 없다.
        if (paymentStatus == Payment.Status.PENDING && nextStatus == Order.OrderStatus.DELIVERING) {
            throw new IllegalArgumentException("결제 대기인 상태에서는 배달중으로 상태를 변경할 수 없습니다.");
        }

        // 무조건 비즈니스 흐름을 타도록 한다. 배달완료에서 주문대기로 바꿀 수는 없다.
        // 순차적으로 변경해야 된다.
        int currentIndex = orderStatusList.indexOf(orderStatus);
        int nextIndex = orderStatusList.indexOf(nextStatus);

        if (Math.abs(currentIndex - nextIndex) != 1) {
            throw new IllegalArgumentException(
                    String.format("'%s' 상태에서는 '%s' 상태로 변경할 수 없습니다. 상태 변경은 순차적으로만 가능합니다.", orderStatus, nextStatus));
        }

        order.changeStatus(nextStatus);  // 상태 변경 수행
        return new ChangeForceStatusResponse(nextStatus);
    }


    @Transactional
    public Object deleteOrder(LoginUser loginUser, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문은 존재하지 않습니다."));

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 결제는 존재하지 않습니다."));

        // 결제 상태인 경우에 주문을 삭제하는 경우에는 결제 상태 초기화
        //TODO 실제 환불 처리는 일어나지 않습니다.
        if (payment.getStatus() == Payment.Status.PAID) {
            payment.delete(loginUser.getUserId());
        }

        order.delete(loginUser.getUserId());
        return null;
    }

    public AdminOrderListResponse getOrderList(Pageable pageable, AdminOrderCondition condition) {
        Page<Order> orders = orderRepository.findByAdmin(pageable, condition);
        return new AdminOrderListResponse(orders);
    }
}
