package com.sparta.bedelivery.AIInteraction.controller;

import com.sparta.bedelivery.AIInteraction.dto.AIInteractionRequest;
import com.sparta.bedelivery.AIInteraction.dto.AIInteractionResponse;
import com.sparta.bedelivery.AIInteraction.service.GeminiInteractionService;
import com.sparta.bedelivery.global.response.ApiResponseData;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIInteractionController {

    private final GeminiInteractionService geminiInteractionService;

    @PostMapping("/interactions")
    public ResponseEntity<ApiResponseData<AIInteractionResponse>> getAIInteractionText(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AIInteractionRequest requestDTO) {

        return ResponseEntity.ok(ApiResponseData.success(geminiInteractionService.processChatInteraction(userDetails.getUsername(), requestDTO)));
    }
}