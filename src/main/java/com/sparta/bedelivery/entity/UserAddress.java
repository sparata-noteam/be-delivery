package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_user_addresses")
public class UserAddress extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public UserAddress(User user,
                       String addressName,
                       String recipientName,
                       String address,
                       String zipCode,
                       Boolean isDefault) {
        super();
        this.user = user;
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.address = address;
        this.zipCode = zipCode;
        this.isDefault = isDefault;
    }

    public void updateAddress( String addressName,
                               String recipientName,
                               String address,
                               String zipCode,
                               Boolean isDefault) {
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.address = address;
        this.zipCode = zipCode;
        this.isDefault = isDefault;
    }
}
