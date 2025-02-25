package com.sparta.bedelivery.dto.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AIInteractionRequest {
    @NotBlank(message = "메세지가 전달되어야합니다.")
    @Size(min=1, max=50, message = "1자이상 50자 이하로 입력해주세요.")
    private String text;
}
