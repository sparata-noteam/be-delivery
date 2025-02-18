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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreatePaymentResponse create(LoginUser loginUser, CreatePaymentRequest createPaymentRequest) {
        UUID orderId = createPaymentRequest.getOrderId();

        User user = userRepository.findByUserId(loginUser.getUserId()).orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 결제는 진행중이 아닙니다."));

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문은 존재하지 않습니다."));

        // 결제 시작
        // 과금 추가
        payment.start(user.getUserId(), createPaymentRequest);

        BigDecimal totalPrice = order.getTotalPrice();
        payment.checkAmount(totalPrice);
        BigDecimal reminder = createPaymentRequest.getAmount().subtract(order.getTotalPrice());
        //TODO 각 결제 API 를 통해 실 결제를 진핸한다.

        return new CreatePaymentResponse(payment, reminder);
    }

    @Transactional
    public PaymentRefundResponse refund(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 결재가 존재하지 않습니다."));

        if (order.getStatus() != Order.OrderStatus.COMPLETED && order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("완료상태인 주문만 환불이 가능합니다.");
        }

        // 결제가 진행중인 경우에만 환불이 가능하다.
        if(payment.getStatus() != Payment.Status.PAID) {
            throw new IllegalArgumentException("결제를 진행중인 경우에만 환불이 가능합니다.");
        }

        payment.refund();
        return new PaymentRefundResponse(order.getStatus(), payment);
    }


    @Transactional
    public PaymentCancelResponse cancel(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("해당하는 결재가 존재하지 않습니다."));

        if (payment.getStatus() != Payment.Status.PAID) {
            throw new IllegalArgumentException("결제 상태가 PAID일 때만 가능합니다.");
        }

        // 경제 취소
        payment.cancel();

        return new PaymentCancelResponse(payment);
    }
}
