package com.sparta.bedelivery.dto.review;

import com.sparta.bedelivery.entity.Review;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReviewCreateResponse {
    UUID reviewId;
    Integer rating;
    String comment;
    List<String> menuList;

    public ReviewCreateResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.menuList = review.getOrder().getMenuNames();
    }
}
