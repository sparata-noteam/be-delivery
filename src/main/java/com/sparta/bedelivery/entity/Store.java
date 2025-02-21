package com.sparta.bedelivery.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_stores") // 매장 테이블
@NoArgsConstructor
public class Store extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Status status = Status.PENDING;

    public enum Status {
        PENDING, OPEN, CLOSED, DELETE_REQUESTED, DELETE, UPDATED, COMPLETED, UPDATE_REQUESTED
    }

    @Builder
    public Store(User userId, String name, LocationCategory locationCategory,
                 String address, String phone, String imageUrl, List<StoreIndustryCategory> storeIndustryCategories,
                 Status status) {
        this.user = userId;  // 변수명은 userId지만 실제로는 User 객체를 받음
        this.name = name;
        this.locationCategory = locationCategory;
        this.address = address;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.storeIndustryCategories = storeIndustryCategories != null ? storeIndustryCategories : new ArrayList<>();
        this.status = status;
    }

    // 수정 요청 승인 데이터 저장용
    public void update(String name, String address, String phone, String imageUrl) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<StoreIndustryCategory> storeIndustryCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_category_id")
    private LocationCategory locationCategory;

    @OneToMany(mappedBy = "store")
    private List<Menu> menuList = new ArrayList<>(); // 매장 테이블에서만 메뉴 테이블을 참조할 수 있게 단방향 관계로 설정함.

    @OneToMany(mappedBy = "store") // store 와 order 는 1 : N 양방향 관계
    private List<Order> orderList = new ArrayList<>();
}