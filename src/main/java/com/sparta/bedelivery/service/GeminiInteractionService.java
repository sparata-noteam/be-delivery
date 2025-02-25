package com.sparta.bedelivery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.dto.ai.AIInteractionRequest;
import com.sparta.bedelivery.dto.ai.AIInteractionResponse;
import com.sparta.bedelivery.dto.ai.GeminiResponse;
import com.sparta.bedelivery.entity.AIInteraction;
import com.sparta.bedelivery.repository.AIInteractionRepository;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.UserRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeminiInteractionService {

    private final WebClient webClient;
    private final AIInteractionRepository aiInteractionRepository;
    private final UserRepository userRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String PROMPT_TEMPLATE = "50자 이내로 답변하세요. \"%s\"";

    // 생성자 메서드
    public GeminiInteractionService(WebClient.Builder webClientBuilder, AIInteractionRepository aiInteractionRepository,
                                    UserRepository userRepository){
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com/v1beta").build();
        this.aiInteractionRepository = aiInteractionRepository;
        this.userRepository = userRepository;
    }

    // 메인 로직: API 요청 -> 응답 메시지 추출 -> DB 저장 -> DTO 반환
    @Transactional
    public AIInteractionResponse processChatInteraction(String userId, AIInteractionRequest requestDTO) {
        // todo - '비동기 작업으로 고민해볼것'
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String prompt = String.format(PROMPT_TEMPLATE, requestDTO.getText());
        String responseData = sendRequestToGemini(prompt).block(); //gemini로부터 요청 응답
        String responseText = extractTextFromResponse(responseData); //받은 응답에서 text만 추출
        saveInteraction(user, requestDTO.getText(), responseText); // 해당 text를 DB에 저장

        return new AIInteractionResponse(responseText);

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
            String responseText = geminiResponse.getResponseText();

            if(responseText==null){
                throw new ResourceAccessException("Gemini로부터 메세지 추출에 실패하였습니다.");
            }

            return responseText;
        } catch (JsonProcessingException e) {
            throw new ResourceAccessException("Gemini로부터 적절한 메세지를 응답받지 못하였습니다.");
        }
    }

    // 상호작용을 DB에 저장하는 메서드
    private AIInteraction saveInteraction(User user, String queryText, String responseText) {
        return aiInteractionRepository.save(new AIInteraction(user, queryText, responseText));
    }

}
