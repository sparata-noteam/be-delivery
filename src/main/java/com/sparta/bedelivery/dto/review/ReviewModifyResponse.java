package com.sparta.bedelivery.dto.review;

import com.sparta.bedelivery.entity.Review;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewModifyResponse {
    UUID reviewId;
    Integer rating;
    String comment;
    List<String> menuList;

    public ReviewModifyResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.menuList = review.getOrder().getMenuNames();
    }
}
