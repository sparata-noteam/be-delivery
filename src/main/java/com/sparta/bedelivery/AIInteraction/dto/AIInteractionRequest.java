package com.sparta.bedelivery.AIInteraction.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AIInteractionRequest {
    @Size(min=1, max=50, message = "1자이상 50자 이하로 입력해주세요.")
    private String text;
}
