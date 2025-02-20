package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_menus")
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false) // 연관 관계 추가
    private Store store;

    @OneToMany(mappedBy = "menu")
    private List<MenuImage> imageList = new ArrayList<>(); // 메뉴 이미지 테이블에 1 : N 단방향 관계 메뉴 하나가 여러 개의 이미지

    @OneToMany(mappedBy = "menu")
    private List<Review> reviewList = new ArrayList<>();

    @Builder
    public Menu(String name, BigDecimal price, String description, Boolean isHidden, Store store) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.isHidden = isHidden;
        this.store = store;
    }
}

