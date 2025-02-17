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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Status status;

    public enum Status {
        OPEN, CLOSED, SUSPENDED
    }

    @Builder
    public Store(String name, String address, String phone, String imageUrl, List<StoreIndustryCategory> storeIndustryCategories) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.storeIndustryCategories = storeIndustryCategories;
    }

    @OneToMany(mappedBy = "store")
    private List<StoreIndustryCategory> storeIndustryCategories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "location_category_id", nullable = false)
    private LocationCategory locationCategory;

    @OneToMany
    @JoinColumn(name = "menu_id", nullable = false)
    private List<Menu> menuList = new ArrayList<>(); // 매장 테이블에서만 메뉴 테이블을 참조할 수 있게 단방향 관계로 설정함.

    @OneToMany
    @JoinColumn(name = "p_stores_id") // 외래 키의 주인은 나 지만 너가 갖고있어 관리는 내가 할 거야
    // 1의 관계
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "store") // store 와 order 는 1 : N 양방향 관계
    private List<Order> orderList = new ArrayList<>();
}