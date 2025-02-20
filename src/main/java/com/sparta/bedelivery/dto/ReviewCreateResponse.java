package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.UUID;

public class ReviewCreateResponse {
    UUID reviewId;
    Integer rating;
    String comment;

    public ReviewCreateResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }
}
