package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Payment;
import com.sparta.bedelivery.entity.PaymentDetail;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;


@Getter
@Setter
public class PaymentResponse {
    private String paymentId;
    private BigInteger amount;
    private PaymentDetail.Method method;
    private Payment.Status status;
}
