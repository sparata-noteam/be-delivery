package com.sparta.bedelivery.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCreateRequest {

    @NotNull(message = "주문 정보를 전달해 주세요.")
    UUID orderId;

    @NotNull
    @Min(value = 0, message = "별접은 0~5정이어야 합니다.")
    @Max(value = 5, message = "별접은 0~5정이어야 합니다.")
    Integer rating;

    @NotBlank(message = "댓글을 입력해 주세요.")
    @Size(min = 0,max = 50, message = "0자이상 50자 이하로 입력해 주세요.")
    String comment;
}
