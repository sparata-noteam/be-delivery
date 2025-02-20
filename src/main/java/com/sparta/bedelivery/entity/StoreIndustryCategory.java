package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "p_store_industry_categories")
@IdClass(StoreIndustryCategoryId.class)
public class StoreIndustryCategory {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn() // store UUID를 참조 // p_stores 테이블에 불필요한 store_id가 또 생겨서 삭제했습니다.
    private Store store;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_category_id") // industryCategory UUID를 참조
    private IndustryCategory industryCategory;
}
