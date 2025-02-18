package com.sparta.bedelivery.review.controller;

import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.review.dto.ReviewCreateRequest;
import com.sparta.bedelivery.review.dto.ReviewResponse;
import com.sparta.bedelivery.review.dto.StoreReviewResponse;
import com.sparta.bedelivery.review.dto.UserReviewResponse;
import com.sparta.bedelivery.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 6.1 리뷰 작성
    @PostMapping
    public ResponseEntity<ApiResponseData<ReviewResponse>> createReview(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ReviewCreateRequest reviewCreateRequest){
        ReviewResponse reviewResponse = reviewService.createReview(userDetails.getUsername(), reviewCreateRequest);
        return ResponseEntity.ok(ApiResponseData.success(reviewResponse,"리뷰가 작성 되었습니다."));
    }

    //6.2 매장 리뷰 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponseData<List<StoreReviewResponse>>> getStoreReviews(@PathVariable UUID storeId){
        return ResponseEntity.ok(ApiResponseData.success(reviewService.getStoreReivews(storeId),"정상적으로 조회 되었습니디ㅏ."));
    }
    

}
