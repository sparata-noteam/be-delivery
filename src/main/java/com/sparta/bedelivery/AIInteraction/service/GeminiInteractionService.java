package com.sparta.bedelivery.AIInteraction.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.AIInteraction.dto.AIInteractionRequest;
import com.sparta.bedelivery.AIInteraction.dto.AIInteractionResponse;
import com.sparta.bedelivery.AIInteraction.dto.GeminiResponse;
import com.sparta.bedelivery.AIInteraction.entity.AIInteraction;
import com.sparta.bedelivery.AIInteraction.repository.AIInteractionRepository;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.UserRepository;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeminiInteractionService implements AIInteractionService {

    private final WebClient webClient;
    private final AIInteractionRepository aiInteractionRepository;
    private final UserRepository userRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String PROMPT_TEMPLATE = "50자 이내로 답변하세요. \"%s\"";

    public GeminiInteractionService(WebClient.Builder webClientBuilder, AIInteractionRepository aiInteractionRepository,
                                    UserRepository userRepository){
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com/v1beta").build();
        this.aiInteractionRepository = aiInteractionRepository;
        this.userRepository = userRepository;
    }

    @Async
    public CompletableFuture<AIInteractionResponse> processChatInteractionAsync(String userId, AIInteractionRequest requestDTO) {
        return CompletableFuture.supplyAsync(() -> processChatInteraction(userId, requestDTO));
    }

    // 메인 로직: API 요청 -> 응답 메시지 추출 -> DB 저장 -> DTO 반환
    @Transactional
    public AIInteractionResponse processChatInteraction(String userId, AIInteractionRequest requestDTO) {
        // todo - '비 블로킹 컨텍스트에서 호출을 막는 것은 스레드에서 기아 상태를 일으킬 수 있습니다' 고민해볼것
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String prompt = PROMPT_TEMPLATE + requestDTO.getText();
        return sendRequestToGemini(prompt)
                .map(response -> extractTextFromResponse(response))
                .map(text -> {
                    saveInteraction(user, requestDTO.getText(), text);
                    return new AIInteractionResponse(text);
                }).block();
    }

    // WebClient를 활용해서 Gemini에게 API 요청
    private Mono<String> sendRequestToGemini(String prompt){
        String url = "/models/gemini-1.5-flash:generateContent?key=" + apiKey;
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    // 응답에서 텍스트만 추출하는 메서드
    private String extractTextFromResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GeminiResponse geminiResponse = objectMapper.readValue(jsonResponse, GeminiResponse.class);
            return geminiResponse.getResponseText();
        } catch (Exception e){
            System.out.println("에러발생");
            throw new ResourceAccessException("Gemini로부터 적절한 메세지를 응답받지 못하였습니다.");
        }
    }

    // 상호작용을 DB에 저장하는 메서드
    private void saveInteraction(User user, String queryText, String responseText) {
        aiInteractionRepository.save(new AIInteraction(user, queryText, responseText));
    }

}
