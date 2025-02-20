package com.sparta.bedelivery.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewModifyRequest {
    @NotNull
    @Min(value = 0, message = "별접은 0~5정이어야 합니다.")
    @Max(value = 5, message = "별접은 0~5정이어야 합니다.")
    Integer rating;

    @NotBlank(message = "댓글을 입력해 주세요.")
    @Size(min = 0,max = 50, message = "0자이상 50자 이하로 입력해 주세요.")
    String comment;

}
