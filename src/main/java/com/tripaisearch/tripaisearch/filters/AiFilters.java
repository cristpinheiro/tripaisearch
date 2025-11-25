package com.tripaisearch.tripaisearch.filters;

import java.time.LocalDateTime;

public record AiFilters(
    LocalDateTime start,
    LocalDateTime end,
    Integer minPassengers,
    Integer maxPassengers,
    Integer pickupZone
) {}