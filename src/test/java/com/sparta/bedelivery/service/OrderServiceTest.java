package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.order.*;
import com.sparta.bedelivery.dto.user.LoginUser;
import com.sparta.bedelivery.entity.*;
import com.sparta.bedelivery.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderService orderService;

    // 공통 테스트 데이터
    private LoginUser loginUser;
    private CreateOrderRequest createOrderRequest;
    private User user;
    private Store store;
    private List<Menu> menuList;
    private List<OrderItemRequest> orderItems;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID menuId = UUID.randomUUID();

        loginUser = new LoginUser("admin", User.Role.CUSTOMER);
        user = User.builder().userId("admin").name("testUser").build();
        // openStatus가 true면 주문 생성 시 가게 오픈 체크에서 Optional.empty()를 받아야 하므로,
        // 테스트에서는 별도 상황에 따라 별도의 객체를 생성하거나 값을 변경.
        store = Store.builder().name("Test Store").status(Store.Status.OPEN).build();

        Menu menu = Menu.builder().id(menuId).name("Test Menu").isHidden(false).price(BigDecimal.valueOf(1000)).build();
        menuList = List.of(menu);
        orderItems = List.of(new OrderItemRequest(menuId, 2));
        createOrderRequest = CreateOrderRequest.builder().storeId(storeId).item(orderItems).build();
    }

    // ------------------- create() -------------------
    @Test
    void createOrder_success() {
        // 계정 조회
        when(userRepository.findByUserId(loginUser.getUserId())).thenReturn(Optional.of(user));
        // 가게 오픈 여부 체크: openStatus 메서드가 open 상태이면 Optional.empty() 반환
        when(storeRepository.findByIdAndDeleteAtIsNullAndOpenStatus(createOrderRequest.getStoreId()))
                .thenReturn(Optional.empty());
        // 메뉴 조회
        when(menuRepository.findAllById(anyList())).thenReturn(menuList);
        // 가게 조회
        when(storeRepository.findById(createOrderRequest.getStoreId()))
                .thenReturn(Optional.of(store));
        // 주문 저장: 인자로 전달된 Order 객체를 그대로 반환
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateOrderResponse response = orderService.create(loginUser, createOrderRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStoreName()).isEqualTo(store.getName());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void createOrder_fail_userNotFound() {
        when(userRepository.findByUserId(loginUser.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(loginUser, createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 계정은 존재하지 않습니다.");
    }

    @Test
    void createOrder_fail_storeNotOpen() {
        // 주문 생성 시 가게가 닫혀있으면 storeRepository에서 값을 반환하도록 처리
        when(userRepository.findByUserId(loginUser.getUserId())).thenReturn(Optional.of(user));
        when(storeRepository.findByIdAndDeleteAtIsNullAndOpenStatus(createOrderRequest.getStoreId()))
                .thenReturn(Optional.of(store));

        assertThatThrownBy(() -> orderService.create(loginUser, createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가게는 오픈된 상태만 주문을 넣을 수 있습니다.");
    }

    // ------------------- getCustomerOrderList() -------------------
    @Test
    void getCustomerOrderList_success() {
        CustomerOrderRequest condition = new CustomerOrderRequest(user.getUserId(), UUID.randomUUID().toString(), Order.OrderStatus.PENDING);
        Page<Order> orderPage = mock(Page.class);

        when(userRepository.findByUserId(condition.getUserId())).thenReturn(Optional.of(user));
        when(orderRepository.findAllOrdersByCustomer(any(Pageable.class), eq(condition)))
                .thenReturn(orderPage);

        CustomerOrderResponse response = orderService.getCustomerOrderList(Pageable.unpaged(), condition);
        assertThat(response).isNotNull();
    }

    @Test
    void getCustomerOrderList_fail_userNotFound() {
        CustomerOrderRequest condition = new CustomerOrderRequest(user.getUserId(), null, null);

        when(userRepository.findByUserId(condition.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getCustomerOrderList(Pageable.unpaged(), condition))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 계정이 존재하지 않습니다.");
    }

    // ------------------- getOwnerOrderList() -------------------
    @Test
    void getOwnerOrderList_success() {
        OwnerOrderRequest condition = new OwnerOrderRequest(UUID.randomUUID().toString(), Order.OrderStatus.PENDING);
        Page<Order> orderPage = mock(Page.class);

        when(orderRepository.findAllOwner(any(Pageable.class), eq(condition)))
                .thenReturn(orderPage);

        OwnerOrderListResponse response = orderService.getOwnerOrderList(Pageable.unpaged(), condition);
        assertThat(response).isNotNull();
    }



    @Test
    void getDetails_fail_orderNotFound() {
        String orderIdStr = UUID.randomUUID().toString();
        when(orderRepository.findById(UUID.fromString(orderIdStr)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getDetails(orderIdStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 주문이 존재하지 않습니다.");
    }

    @Test
    void getDetails_fail_paymentNotFound() {
        String orderIdStr = UUID.randomUUID().toString();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        when(orderRepository.findById(UUID.fromString(orderIdStr)))
                .thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(UUID.fromString(orderIdStr)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getDetails(orderIdStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 결제가 존재하지 않습니다.");
    }

    // ------------------- accept() -------------------
    @Test
    void accept_success() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        // 초기 상태는 PENDING (아직 confirm 처리 안됨)
        orderFromRepo.changeStatus(Order.OrderStatus.PENDING);
        Payment paymentFromRepo = new Payment(orderFromRepo);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(paymentFromRepo));

        OrderAcceptResponse response = orderService.accept(orderId);
        assertThat(response).isNotNull();
    }

    @Test
    void accept_fail_orderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.accept(orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 주문이 존재하지 않습니다.");
    }

    @Test
    void accept_fail_alreadyConfirmed() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        orderFromRepo.changeStatus(Order.OrderStatus.CONFIRMED);
        Payment paymentFromRepo = new Payment(orderFromRepo);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(paymentFromRepo));

        assertThatThrownBy(() -> orderService.accept(orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 확인이 완료되었습니다.");
    }

    @Test
    void accept_fail_cancelled() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        orderFromRepo.changeStatus(Order.OrderStatus.CANCELLED);
        Payment paymentFromRepo = new Payment(orderFromRepo);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(paymentFromRepo));

        assertThatThrownBy(() -> orderService.accept(orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("취소된 주문입니다.");
    }

    @Test
    void accept_fail_paymentNotPending() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        orderFromRepo.changeStatus(Order.OrderStatus.PENDING);
        Payment paymentFromRepo = new Payment(orderFromRepo);
        // Payment 상태를 PENDING이 아닌 PAID로 강제 설정 (spy 활용)
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PAID).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(spyPayment));

        assertThatThrownBy(() -> orderService.accept(orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 대기인 상태인 주문만 취소가 가능합니다.");
    }

    // ------------------- status() -------------------
    @Test
    void status_success() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        orderFromRepo.changeStatus(Order.OrderStatus.PENDING);
        Payment paymentFromRepo = new Payment(orderFromRepo);
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PAID).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(spyPayment));

        // 예를 들어 PENDING -> CONFIRMED 로 변경 (순차적 변경)
        OrderStatusResponse response = orderService.status(orderId, Order.OrderStatus.CONFIRMED);
        assertThat(response).isNotNull();
    }

    @Test
    void status_fail_paymentNotPaid() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        orderFromRepo.changeStatus(Order.OrderStatus.PENDING);
        Payment paymentFromRepo = new Payment(orderFromRepo);
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PENDING).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(spyPayment));

        assertThatThrownBy(() -> orderService.status(orderId, Order.OrderStatus.CONFIRMED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태는 결제 진행중인 경우에만 변경이 가능합니다.");
    }

    // ------------------- cancel() -------------------
    @Test
    void cancel_success_customer() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        // 주문 생성 시점이 현재로 설정되어 있다고 가정 (5분 이내)
        Payment paymentFromRepo = new Payment(orderFromRepo);
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PENDING).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(spyPayment));

        LoginUser customer = new LoginUser(user.getUserId(), User.Role.CUSTOMER);
        OrderCancelResponse response = orderService.cancel(customer, orderId);
        assertThat(response).isNotNull();
    }

    @Test
    void cancel_fail_orderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.cancel(loginUser, orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 주문이 존재하지 않습니다.");
    }

    @Test
    void cancel_fail_paymentNotFound() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.cancel(loginUser, orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해단하는 결제는 존재하지 않습니다.");
    }

    @Test
    void cancel_fail_afterFiveMinutes() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        // 주문 생성 시각을 6분 전으로 설정
        orderFromRepo.setOrderedAt(LocalDateTime.now().minusMinutes(6));
        Payment paymentFromRepo = new Payment(orderFromRepo);
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PENDING).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(spyPayment));

        assertThatThrownBy(() -> orderService.cancel(loginUser, orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 생성후 5분이내에만 취소가 가능합니다.");
    }

    // ------------------- forceChange() -------------------
    @Test
    void forceChange_success_cancelPending() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        // 초기 상태 PENDING
        orderFromRepo.changeStatus(Order.OrderStatus.PENDING);
        Payment paymentFromRepo = new Payment(orderFromRepo);
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PENDING).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(spyPayment));

        // PENDING에서 CANCELLED로 변경
        ChangeForceStatusResponse response = orderService.forceChange(orderId, Order.OrderStatus.CANCELLED);
        assertThat(response).isNotNull();
    }

    @Test
    void forceChange_success_cancelPaid() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        orderFromRepo.changeStatus(Order.OrderStatus.PENDING);
        Payment paymentFromRepo = new Payment(orderFromRepo);
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PAID).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(spyPayment));

        ChangeForceStatusResponse response = orderService.forceChange(orderId, Order.OrderStatus.CANCELLED);
        assertThat(response).isNotNull();
        verify(spyPayment, times(1)).initStatus();
    }

    // ------------------- deleteOrder() -------------------
    @Test
    void deleteOrder_success_paidPayment() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        Payment paymentFromRepo = new Payment(orderFromRepo);
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PAID).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(spyPayment));

        Object response = orderService.deleteOrder(loginUser, orderId);
        verify(spyPayment, times(1)).delete(loginUser.getUserId());
        assertThat(response).isNull();
    }

    @Test
    void deleteOrder_success_nonPaidPayment() {
        UUID orderId = UUID.randomUUID();
        Order orderFromRepo = new Order(createOrderRequest, BigDecimal.valueOf(1000));
        Payment paymentFromRepo = new Payment(orderFromRepo);
        Payment spyPayment = spy(paymentFromRepo);
        doReturn(Payment.Status.PENDING).when(spyPayment).getStatus();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderFromRepo));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(spyPayment));

        Object response = orderService.deleteOrder(loginUser, orderId);
        // 결제 상태가 PENDING이면 Payment.delete()는 호출되지 않아야 함.
        verify(spyPayment, times(0)).delete(any());
        assertThat(response).isNull();
    }

    // ------------------- getOrderList() -------------------
    @Test
    void getOrderList_success() {
        AdminOrderCondition condition = new AdminOrderCondition(Order.OrderStatus.CONFIRMED, UUID.randomUUID().toString(), false);
        Page<Order> orderPage = mock(Page.class);

        when(orderRepository.findByAdmin(any(Pageable.class), eq(condition)))
                .thenReturn(orderPage);

        AdminOrderListResponse response = orderService.getOrderList(Pageable.unpaged(), condition);
        assertThat(response).isNotNull();
    }
}
