package com.sparta.bedelivery.review.dto;

import com.sparta.bedelivery.entity.Review;
import java.util.List;
import java.util.UUID;

public class AdminReviewResponse {
    List<StoreReviewResponse> storeReviewResponseList; //매장별 모든 리뷰를 조회

    public AdminReviewResponse(List<Review> reviews) {
    }
}
