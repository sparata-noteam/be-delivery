package com.sparta.bedelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreIndustryCategoryId implements Serializable { // 직렬화가 가능한 클래스로 기본 키 클래스로 정의함.
    private UUID store;
    private UUID industryCategory;
}

