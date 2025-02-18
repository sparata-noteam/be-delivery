package com.sparta.bedelivery.review.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.UUID;

public class AdminReviewResponse {
    UUID reviewId;
    Integer rating;
    String comment;
    UUID storeId; //매장 ID
    String storeName; //매장 이름
    Long writerId; //작성자 ID(PK값)

    public AdminReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.storeId = review.getStore().getId();
        this.storeName = review.getStore().getName();
    }
}
