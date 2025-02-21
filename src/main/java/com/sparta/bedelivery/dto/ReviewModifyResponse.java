package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
