package com.sparta.bedelivery.review.controller;

import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.review.dto.ReviewCreateRequest;
import com.sparta.bedelivery.review.dto.ReviewModifyRequest;
import com.sparta.bedelivery.review.dto.ReviewCreateResponse;
import com.sparta.bedelivery.review.dto.ReviewModifyResponse;
import com.sparta.bedelivery.review.dto.StoreReviewResponse;
import com.sparta.bedelivery.review.dto.UserReviewResponse;
import com.sparta.bedelivery.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 6.1 리뷰 작성
    @PostMapping
    public ResponseEntity<ApiResponseData<ReviewCreateResponse>> createReview(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ReviewCreateRequest reviewCreateRequest)
    {
        ReviewCreateResponse reviewCreateResponse = reviewService.createReview(userDetails.getUsername(), reviewCreateRequest);
        return ResponseEntity.ok(ApiResponseData.success(reviewCreateResponse,"리뷰가 작성 되었습니다."));
    }

    //6.2 매장 리뷰 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponseData<Page<StoreReviewResponse>>> getStoreReviews(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size)
    {
        Page<StoreReviewResponse> reviews = reviewService.getStoreReviews(storeId, page, size);
        return ResponseEntity.ok(ApiResponseData.success(reviews, "정상적으로 리뷰가 조회되었습니다."));
    }


    //6.3 사용자 리뷰 조회(사용자가 작성한 리뷰 전체 조회)
    @GetMapping
    public ResponseEntity<ApiResponseData<Page<UserReviewResponse>>> getUserReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<UserReviewResponse> reviews = reviewService.getUserReviews(userDetails.getUsername(), page, size);
        return ResponseEntity.ok(ApiResponseData.success(reviews, "정상적으로 리뷰가 조회되었습니다."));
    }


    //6.4 사용자 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponseData<ReviewModifyResponse>> modifyReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID reviewId,
            @Valid @RequestBody ReviewModifyRequest reviewModifyRequest) {
        ReviewModifyResponse reviewModifyResponse = reviewService.modifyReview(userDetails.getUsername(), reviewId, reviewModifyRequest);
        return ResponseEntity.ok(ApiResponseData.success(reviewModifyResponse, "리뷰가 수정되었습니다."));
    }

    //6.5 리뷰 삭제(사용자용)
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponseData<String>> deleteReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID reviewId) {
        reviewService.deleteReview(userDetails.getUsername(), reviewId);
        return ResponseEntity.ok(ApiResponseData.success(null, "리뷰가 삭제되었습니다."));
    }

}
