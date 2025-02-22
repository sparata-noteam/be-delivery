package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class StoreReviewResponse {
    UUID reviewId;
    Integer rating;
    String comment;
    String userNickname;
    List<String> menuList;

    public StoreReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.userNickname = review.getUser().getNickname();
        this.menuList = review.getOrder().getMenuNames();
    }
}
