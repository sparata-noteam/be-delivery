package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_menu_images")
// menu_id (FK) 를 갖게된다. 키의 주인은 Menu 테이블이지만 잠시 맡아놓는 개념.
public class MenuImage extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private Integer orderIndex = 0;
}

