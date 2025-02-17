package com.sparta.bedelivery.AIInteraction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponse {

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<Candidate> candidates;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        private Content content;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {

        private String text;
    }

    public String getResponseText() {
        if (candidates == null || candidates.isEmpty()) {
            return "응답을 처리할 수 없습니다.";
        }

        Candidate candidate = candidates.get(0);
        if (candidate == null || candidate.getContent() == null) {
            return "응답을 처리할 수 없습니다.";
        }

        Content content = candidate.getContent();
        List<Part> parts = content.getParts();

        if (parts == null || parts.isEmpty()) {
            return "응답을 처리할 수 없습니다.";
        }

        Part part = parts.get(0);
        if (part == null || part.getText() == null) {
            return "응답을 처리할 수 없습니다.";
        }

        return part.getText();
    }
}
