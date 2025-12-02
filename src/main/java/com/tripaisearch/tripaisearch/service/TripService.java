package com.tripaisearch.tripaisearch.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;

import com.tripaisearch.tripaisearch.entity.Trip;
import com.tripaisearch.tripaisearch.model.AiFilters;

public interface TripService {

    /**
     * Find trip by id
     * 
     * @param id the id of the trip
     * @return an Optional containing the found trip or empty if not found
     */
    public Optional<Trip> findById(Long id);

    /**
     * Find all trips by prompt
     * 
     * @param prompt the prompt to apply for AI-based filtering
     * @return a list of all trips
     */
    public List<Trip> findByPrompt(String prompt);

    /**
     * Save a trip
     * 
     * @param trip the trip to save
     * @return the saved trip
     */
    public Trip save(Trip trip);

    /**
     * Delete a trip by id
     * 
     * @param id the id of the trip to delete
     */
    public void deleteById(Long id);

    /**
     * AI-based route generation
     * 
     * @param body the input text for route generation
     * @return the generated route object
     */
    public Object aiRoute(String body);

    /**
     * Build a Specification<Trip> from given filters
     * 
     * @param filters the AI filters
     * @return the constructed Specification<Trip>
     */
    public Specification<Trip> buildSpecificationFromFilters(AiFilters filters);
}
