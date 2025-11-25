package com.tripaisearch.tripaisearch.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripaisearch.tripaisearch.entity.Trip;
import com.tripaisearch.tripaisearch.service.AiFilterService;
import com.tripaisearch.tripaisearch.service.TripSearchService;

@RestController
@RequestMapping("/trips")
public final class TripAiController {

    private final AiFilterService ai;
    private final TripSearchService search;

    public TripAiController(AiFilterService ai, TripSearchService search) {
        this.ai = ai;
        this.search = search;
    }

    @PostMapping("/ai-search")
    public List<Trip> aiSearch(@RequestBody String text) throws Exception {
        var filters = ai.parse(text);
        return search.search(filters);
    }
}
