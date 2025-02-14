package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_user_addresses") // 한 명의 유저가 여러 개의 배송지를 저장할 수 있다.
public class UserAddress extends BaseSystemFieldEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 255)
    private String addressName;

    @Column(nullable = false, length = 100)
    private String recipientName;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 10)
    private String zipCode;

    @Column(nullable = false)
    private Boolean isDefault = false;
}
