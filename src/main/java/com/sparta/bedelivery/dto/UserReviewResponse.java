package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserReviewResponse {
    UUID reviewId;
    Integer rating;
    String comment;
    UUID storeId;
    String storeName;

    public UserReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.storeId = review.getStore().getId();
        this.storeName = review.getStore().getName();
    }
}
