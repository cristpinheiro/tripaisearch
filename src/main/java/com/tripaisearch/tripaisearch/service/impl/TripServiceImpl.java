package com.tripaisearch.tripaisearch.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripaisearch.tripaisearch.entity.Trip;
import com.tripaisearch.tripaisearch.filter.TripSpecs;
import com.tripaisearch.tripaisearch.model.AiFilters;
import com.tripaisearch.tripaisearch.model.AiRoute;
import com.tripaisearch.tripaisearch.repository.TripRepository;
import com.tripaisearch.tripaisearch.service.AiClient;
import com.tripaisearch.tripaisearch.service.TripService;

@Service
public class TripServiceImpl implements TripService {

    private static final String AI_ROUTE_PROMPT = """
            You are a function orchestrator. Read the user message and return ONLY a JSON object:
            { "function": "...", "args": { ... } }

            Available functions:

            1. findById(id: number)
            2. listTrips()
            3. createTrip(pickupDatetime, dropoffDatetime, passengerCount, pickupZone, dropoffZone)

            Rules:
            - Infer the correct function.
            - If user asks for a single trip by ID: findById.
            - If user asks to list/search trips: listTrips.
            - If user wants to register/create a trip: createTrip.
            - If no function applies: { "function": "unknown", "args": {} }

            Examples:

            User: "show trip 10"
            Output: { "function": "findById", "args": { "id": 10 } }

            User: "list all trips"
            Output: { "function": "listTrips", "args": {} }

            User: "create a trip from A to B with 2 passengers"
            Output: {
              "function": "createTrip",
              "args": {
                "pickupZone": "A",
                "dropoffZone": "B",
                "passengerCount": 2
              }
            }

            Now respond to the next user message:

            User:
            """;

    /*
     * Prompt to generate filters for finding all trips
     */
    private static final String AI_FILTER_FIND_ALL_PROMPT = "Generate only valid JSON exactly in this format:\n" +
            "{\n" +
            "  \"start\": null,\n" +
            "  \"end\": null,\n" +
            "  \"minPassengers\": null,\n" +
            "  \"maxPassengers\": null,\n" +
            "  \"pickupZone\": null\n" +
            "}\n\n" +
            "Fill the fields based on the text. The start and end fields must be in the format: yyyy-mm-dd hh:mm:ss. Do not add anything beyond the JSON.\n"
            +
            "Text: \n";

    private final TripRepository tripRepository;
    private final AiClient aiClient;

    public TripServiceImpl(TripRepository tripRepository, AiClient aiClient) {
        this.tripRepository = tripRepository;
        this.aiClient = aiClient;
    }

    /**
     * Find trip by id
     * 
     * @param id the id of the trip
     * @return an Optional containing the found trip or empty if not found
     */
    public Optional<Trip> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return tripRepository.findById(id);
    }

    /**
     * Find all trips by prompt
     * 
     * @param prompt the prompt to apply for AI-based filtering
     * @return a list of all trips
     */
    public List<Trip> findByPrompt(String prompt) {
        if (prompt != null) {
            try {
                String aiResponse = searchAiFilters(prompt);
                var filters = buildAiFilters(aiResponse);
                Specification<Trip> specification = buildSpecificationFromFilters(filters);
                return tripRepository.findAll(specification);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse AI filters", e);
            }
        }
        return tripRepository.findAll();
    }

    /**
     * Save a trip
     * 
     * @param trip the trip to save
     * @return the saved trip
     */
    public Trip save(Trip trip) {
        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null");
        }
        return tripRepository.save(trip);
    }

    /**
     * Delete a trip by id
     * 
     * @param id the id of the trip to delete
     */
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        tripRepository.deleteById(id);
    }

    /**
     * AI-based route generation
     * 
     * @param body the input text for route generation
     * @return the generated route object
     */
    public Object aiRoute(String body) {
        var prompt = AI_ROUTE_PROMPT + body;
        var aiResponse = aiClient.chat(prompt);
        var functionWithParams = parseAiRoute(aiResponse);
        switch (functionWithParams.function()) {
            case "findById":
                Long id = Long.valueOf(String.valueOf(functionWithParams.args().get("id")));
                return findById(id);
            case "listTrips":
                return tripRepository.findAll();
            default:
                throw new IllegalArgumentException("Cannot determine function from AI response");
        }
    }

    private AiRoute parseAiRoute(String response) {
        var mapper = new ObjectMapper();
        try {
            return mapper.readValue(response, AiRoute.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI route", e);
        }
    }

    /*
     * Helper method to get AI-generated filters from prompt
     */
    private AiFilters buildAiFilters(String response) {
        var mapper = new ObjectMapper();
        // Accept dates in the form "yyyy-MM-dd HH:mm:ss" (space between date and time)
        var formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var javaTimeModule = new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule();
        javaTimeModule.addDeserializer(java.time.LocalDateTime.class,
                new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer(formatter));
        mapper.registerModule(javaTimeModule);
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return mapper.readValue(response, AiFilters.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI filters", e);
        }
    }

    private String searchAiFilters(String prompt) {
        if (prompt == "") {
            throw new IllegalArgumentException("Prompt cannot be empty");
        }
        var promptWithSpec = AI_FILTER_FIND_ALL_PROMPT + prompt;
        return aiClient.generate(promptWithSpec);
    }

    /**
     * Build a Specification<Trip> from given filters
     * 
     * @param filters the AI filters
     * @return the constructed Specification<Trip>
     */
    public Specification<Trip> buildSpecificationFromFilters(AiFilters filters) {
        var specs = new ArrayList<Specification<Trip>>();
        if (filters.start() != null)
            specs.add(TripSpecs.start(filters.start()));
        if (filters.end() != null)
            specs.add(TripSpecs.end(filters.end()));
        if (filters.minPassengers() != null)
            specs.add(TripSpecs.minPassengers(filters.minPassengers()));
        if (filters.pickupZone() != null)
            specs.add(TripSpecs.pickupZone(filters.pickupZone()));
        return Specification.allOf(specs);
    }

}
