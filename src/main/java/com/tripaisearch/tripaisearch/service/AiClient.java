package com.tripaisearch.tripaisearch.service;

public interface AiClient {
        /**
         * Generate a response from the AI model based on the given prompt.
         * 
         * @param prompt the input prompt for the AI model
         * @return a JSONObject containing the AI-generated response
         */
        public String generate(String prompt);
}