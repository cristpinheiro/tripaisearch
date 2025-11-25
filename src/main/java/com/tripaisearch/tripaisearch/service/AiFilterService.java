package com.tripaisearch.tripaisearch.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.tripaisearch.tripaisearch.filters.AiFilters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public final class AiFilterService {

    private final AiClient ai;

    public AiFilterService(AiClient ai) {
        this.ai = ai;
    }

    public AiFilters parse(String text) throws JsonMappingException, JsonProcessingException {
        var prompt = "Text: " + text + "\n\n" +
                "Generate only valid JSON exactly in this format:\n" +
                "{\n" +
                "  \"start\": null,\n" +
                "  \"end\": null,\n" +
                "  \"minPassengers\": null,\n" +
                "  \"maxPassengers\": null,\n" +
                "  \"pickupZone\": null\n" +
                "}\n\n" +
                "Fill the fields based on the text. The start and end fields must be in the format: yyyy-mm-dd hh:mm:ss. Do not add anything beyond the JSON.";

        var raw = ai.generate(prompt);

        // parse JSON externo
        var root = new JSONObject(raw);
        var responseText = root.getString("response").trim();

        // remove fences
        responseText = responseText
                .replace("```json", "")
                .replace("```", "")
                .trim();
            
        System.out.println(responseText);

        var mapper = new ObjectMapper();
        // Accept dates in the form "yyyy-MM-dd HH:mm:ss" (space between date and time)
        var formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var javaTimeModule = new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule();
        javaTimeModule.addDeserializer(java.time.LocalDateTime.class,
                new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer(formatter));
        mapper.registerModule(javaTimeModule);
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.readValue(responseText, AiFilters.class);
    }
}
