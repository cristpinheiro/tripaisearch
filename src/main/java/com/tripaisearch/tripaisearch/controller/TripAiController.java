package com.tripaisearch.tripaisearch.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripaisearch.tripaisearch.entity.Trip;
import com.tripaisearch.tripaisearch.service.TripService;

@RestController
@RequestMapping("/trips")
public final class TripAiController {
    private final TripService tripService;

    public TripAiController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/ai-search")
    public List<Trip> aiSearch(@RequestBody String text) throws Exception {
        return tripService.findByPrompt(text);
    }

    @PostMapping("/ai-route")
    public Object aiRoute(@RequestBody String body) throws Exception {
        return tripService.aiRoute(body);
    }
}
