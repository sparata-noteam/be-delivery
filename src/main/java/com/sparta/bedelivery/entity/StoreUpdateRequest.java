package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_store_update_requests")
@NoArgsConstructor
// 매장 수정 요청사항을 담을 엔티티를 하나 생성
public class StoreUpdateRequest extends BaseSystemFieldEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Store.Status status;

    @Builder
    public StoreUpdateRequest(Store storeId, String name, String address, String phone, String imageUrl){
        this.store = storeId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Store store;
}
