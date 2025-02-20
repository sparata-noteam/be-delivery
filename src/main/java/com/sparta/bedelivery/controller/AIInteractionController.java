package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.AIInteractionRequest;
import com.sparta.bedelivery.dto.AIInteractionResponse;
import com.sparta.bedelivery.service.GeminiInteractionService;
import com.sparta.bedelivery.global.response.ApiResponseData;
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
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIInteractionController {

    private final GeminiInteractionService geminiInteractionService;

    @PostMapping("/interactions")
    public ResponseEntity<ApiResponseData<AIInteractionResponse>> getAIInteractionText(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid AIInteractionRequest requestDTO) {

        return ResponseEntity.ok(ApiResponseData.success(geminiInteractionService.processChatInteraction(userDetails.getUsername(), requestDTO)));
    }
}