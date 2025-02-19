package com.sparta.bedelivery.service;

import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MenuRepository menuRepository;

    @MockBean
    private StoreRepository storeRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private PaymentRepository paymentRepository;


    @BeforeEach
    void setUp() {
        Optional<User> user = userRepository.findByUserId("aa");
    }

    @Test
    void create() {
    }

    @Test
    void getCustomerOrderList() {
    }

    @Test
    void getOwnerOrderList() {
    }
}