package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @JoinColumn(name = "store_id", nullable = false) // 연관 관계 추가
    private Store store;

    @OneToMany
    @JoinColumn(name = "p_menus_id") // 외래키의 주인이 외래키를 컨트롤 할 수 있다.
    private List<MenuImage> imageList = new ArrayList<>(); // 메뉴 이미지 테이블에 1 : N 단방향 관계 메뉴 하나가 여러 개의 이미지

    @Builder
    public Menu(String name, BigDecimal price, String description, Boolean isHidden, Store store) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.isHidden = isHidden;
        this.store = store;
    }
}

