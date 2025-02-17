package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_store_industry_categories")
@IdClass(StoreIndustryCategoryId.class) // 복합키를 사용해서 엔티티의 기본 키를 정의
public class StoreIndustryCategory { // 중간 테이블

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_store_id")
    private Store store; // @Id 어노테이션으로 또 다른 UUID 키로 정하지 않고 UUID 로 설정된 값으로 참조?

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_industry_category_id")
    private IndustryCategory industryCategory;
}

