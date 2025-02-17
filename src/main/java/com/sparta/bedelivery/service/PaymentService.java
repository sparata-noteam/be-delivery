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

        Payment savePay = paymentRepository.save(new Payment(user, order));

        //TODO: 결제 상세 정보 저장
        return new CreatePaymentResponse(savePay);
    }

    @Transactional
    public PaymentRefundResponse refund(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("해당하는 결재가 존재하지 않습니다."));

        if (order.getStatus() != Order.OrderStatus.COMPLETED && order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("완료상태인 주문만 환불이 가능합니다.");
        }

        payment.refund();
        return new PaymentRefundResponse(order.getStatus(), payment);
    }


    @Transactional
    public PaymentCancelResponse cancel(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("해당하는 결재가 존재하지 않습니다."));

        if(payment.getStatus() != Payment.Status.PAID) {
            throw new IllegalArgumentException("결제 상태가 PAID일 때만 가능합니다.");
        }

        // 경제 취소
        payment.cancel();

        return new PaymentCancelResponse(payment);
    }
}
