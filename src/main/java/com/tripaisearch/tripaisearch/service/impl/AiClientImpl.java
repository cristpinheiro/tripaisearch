package com.tripaisearch.tripaisearch.service.impl;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.tripaisearch.tripaisearch.service.AiClient;

@Service
public final class AiClientImpl implements AiClient {

    private final RestClient client;

    public AiClientImpl(@Value("${ai.client.token}") String token) {
        this.client = RestClient.builder()
                .baseUrl("https://ollama.com")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    public String generate(String prompt) {
        Map<String, Object> body = Map.of(
                // "model", "gpt-oss:120b-cloud",
                "model", "gpt-oss:20b-cloud",
                "prompt", prompt,
                "stream", false);
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
        var response = client.post()
                .uri("/api/generate")
                .body(body)
                .retrieve()
                .body(String.class);
        return parseResponse(response);
    }

    public String chat(String prompt) {
        Map<String, Object> body = Map.of(
                "model", "gpt-oss:20b-cloud",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)),
                "stream", false);
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
        var response = client.post()
                .uri("/api/chat")
                .body(body)
                .retrieve()
                .body(String.class);
        return extractContent(response);
    }

    private String extractContent(String raw) {
        var json = new JSONObject(raw);
        return json.getJSONObject("message").getString("content").trim();
    }

    /**
     * Helper method to parse the AI response and extract the JSON content.
     * 
     * @param raw the raw response string from the AI
     * @return a JSONObject containing the parsed JSON content
     */
    private String parseResponse(String raw) {
        var root = new JSONObject(raw);
        var responseText = root.getString("response").trim();

        // remove fences
        responseText = responseText
                .replace("```json", "")
                .replace("```", "")
                .trim();

        return responseText;
    }
}
