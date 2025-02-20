package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.CreateOrderRequest;
import com.sparta.bedelivery.dto.CreateOrderResponse;
import com.sparta.bedelivery.dto.LoginUser;
import com.sparta.bedelivery.dto.OrderItemRequest;
import com.sparta.bedelivery.entity.*;
import com.sparta.bedelivery.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mysema.commons.lang.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private LoginUser loginUser;
    private CreateOrderRequest createOrderRequest;
    private User user;
    private Store store;
    private List<Menu> menuList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        loginUser = new LoginUser("admin", User.Role.CUSTOMER);
        createOrderRequest = mock(CreateOrderRequest.class);
        user = mock(User.class);
        store = mock(Store.class);
        menuList.add(mock(Menu.class));
    }

    @Test
    void createOrder_Success() {
        OrderItemRequest itemRequest = mock(OrderItemRequest.class);

        when(userRepository.findByUserId(loginUser.getUserId())).thenReturn(Optional.of(user));

        when(menuRepository.findAllById(any())).thenReturn(menuList);
        when(storeRepository.findById(createOrderRequest.getStoreId())).thenReturn(Optional.of(store));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateOrderResponse response = orderService.create(loginUser, createOrderRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStoreName()).isEqualTo(store.getName());

    }
}