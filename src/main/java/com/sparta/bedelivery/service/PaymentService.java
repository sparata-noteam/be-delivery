package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Payment;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.OrderRepository;
import com.sparta.bedelivery.repository.PaymentRepository;
import com.sparta.bedelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public CreatePaymentResponse create(LoginUser loginUser, CreatePaymentRequest createPaymentRequest) {
        UUID orderId = createPaymentRequest.getOrderId();
        User user = userRepository.findByUserId(loginUser.getUserId()).orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));

        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new IllegalArgumentException("주문 대기인 경우에만 결제를 생성할 수 있습니다.");
        }

        Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);

        if (payment != null) {
            throw new IllegalArgumentException("결재 진행중에 있습니다.");
        }

        Payment savePay = paymentRepository.save(new Payment(createPaymentRequest, user, order));

        return null;
    }

    public PaymentRefundResponse refund(String orderId) {
        return null;
    }


    public PaymentCancelResponse cancel(String paymentId) {
        return null;
    }
}
