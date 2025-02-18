package com.sparta.bedelivery.review.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.UUID;

public class ReviewModifyResponse {
    UUID reviewId;
    Integer rating;
    String comment;

    public ReviewModifyResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }
}
