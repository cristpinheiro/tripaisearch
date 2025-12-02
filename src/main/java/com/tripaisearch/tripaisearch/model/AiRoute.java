package com.tripaisearch.tripaisearch.model;

import java.util.Map;

public record AiRoute(
        String function,
        Map<String, Object> args) {
}
