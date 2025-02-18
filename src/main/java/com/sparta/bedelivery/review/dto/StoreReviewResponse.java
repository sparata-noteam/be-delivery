package com.sparta.bedelivery.review.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.UUID;
import lombok.Getter;

@Getter
public class StoreReviewResponse {
    UUID reviewId;
    Integer rating;
    String comment;
    String writerNickname;

    public StoreReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.writerNickname = review.getUser().getNickname();
    }
}
