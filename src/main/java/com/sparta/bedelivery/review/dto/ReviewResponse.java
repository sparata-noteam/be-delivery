package com.sparta.bedelivery.review.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.UUID;

public class ReviewResponse {
    UUID reviewId;
    Integer rating;
    String comment;

    public ReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }
}
