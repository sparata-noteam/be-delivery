package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_location_categories")
public class LocationCategory extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    // 시, 구, 동 이 UNIQUE 옵션이 들어가 있어서 여러 데이터를 넣지 못함.
    @Column(nullable = false,length = 255)
    private String type;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private LocationCategory parent;

    @OneToMany(mappedBy = "parent")
    private List<LocationCategory> children = new ArrayList<>();

}
