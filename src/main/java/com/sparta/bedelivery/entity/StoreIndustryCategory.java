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
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Id
    @ManyToOne
    @JoinColumn(name = "industry_category_id", nullable = false)
    private IndustryCategory industryCategory;
}

