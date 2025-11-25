package com.tripaisearch.tripaisearch.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Service
public final class AiClient {

        private final RestClient client;

        public AiClient(@Value("${ai.client.token}") String token) {
                this.client = RestClient.builder()
                                .baseUrl("https://ollama.com")
                                .defaultHeader("Authorization", "Bearer " + token)
                                .build();
        }

        @SuppressWarnings("null")
        public String generate(String prompt) {
                Map<String, Object> body = Map.of(
                                "model", "gpt-oss:20b-cloud",
                                "prompt", prompt,
                                "stream", false);

                return client.post()
                                .uri("/api/generate")
                                .body(body)
                                .retrieve()
                                .body(String.class);
        }
}