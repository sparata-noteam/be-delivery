package com.sparta.bedelivery.dto.review;

import com.sparta.bedelivery.entity.Review;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class AdminReviewResponse {

    //매장 정보
    UUID storeId;
    String storeName;

    //리뷰 정보
    UUID reviewId;
    Integer rating;
    String comment;

    //유저 정보
    Long userId; //User의 PK값
    String userNickname;

    //주문한 메뉴 정보
    List<String> menuList;
    public AdminReviewResponse(Review review) {
        this.storeId = review.getStore().getId();
        this.storeName = review.getStore().getName();
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.userNickname = review.getUser().getNickname();
        this.userId = review.getUser().getId();
        this.menuList = review.getOrder().getMenuNames();
    }
}
