package com.sparta.bedelivery.review.controller;

import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.review.dto.ReviewCreateRequest;
import com.sparta.bedelivery.review.dto.ReviewCreateResponse;
import com.sparta.bedelivery.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponseData<ReviewCreateResponse>> createReview(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ReviewCreateRequest reviewCreateRequest){
        ReviewCreateResponse reviewCreateResponse = reviewService.createReview(userDetails.getUsername(), reviewCreateRequest);
        return ResponseEntity.ok(ApiResponseData.success(reviewCreateResponse));
    }

}
