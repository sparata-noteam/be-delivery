package com.sparta.bedelivery.entity;

import com.sparta.bedelivery.dto.ReviewCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_reviews")
public class Review extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_stores_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    //todo- 필요한지 확인해보아야함
    @ManyToOne
    private Menu menu;


    public Review(ReviewCreateRequest reviewCreateRequest, User user, Order order) {
        this.user = user;
        this.order = order;
        this.store = order.getStore();
        this.rating = reviewCreateRequest.getRating();
        this.comment = reviewCreateRequest.getComment();
    }

}

