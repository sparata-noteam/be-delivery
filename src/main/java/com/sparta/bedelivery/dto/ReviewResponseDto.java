package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ReviewResponseDto {

    private UUID reviewId;
    private Long userId;
    private Integer rating;
    private String comment;
    private LocalDateTime createAt;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getId();
        this.userId = review.getUser().getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }
}
