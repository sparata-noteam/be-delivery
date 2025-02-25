package com.sparta.bedelivery.dto.user;

import com.sparta.bedelivery.entity.Review;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserReviewResponse {
    UUID reviewId;
    Integer rating;
    String comment;
    UUID storeId;
    String storeName;
    List<String> menuList;

    public UserReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.storeId = review.getStore().getId();
        this.storeName = review.getStore().getName();
        this.menuList = review.getOrder().getMenuNames();
    }
}
