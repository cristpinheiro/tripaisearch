package com.tripaisearch.tripaisearch.filter;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.tripaisearch.tripaisearch.entity.Trip;

public final class TripSpecs {

    public static Specification<Trip> start(LocalDateTime v) {
        return (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("pickupDatetime"), v);
    }

    public static Specification<Trip> end(LocalDateTime v) {
        return (r, q, cb) -> cb.lessThanOrEqualTo(r.get("pickupDatetime"), v);
    }

    public static Specification<Trip> minPassengers(Integer v) {
        return (r, q, cb) -> cb.ge(r.get("passengerCount"), v);
    }

    public static Specification<Trip> pickupZone(Integer v) {
        return (r, q, cb) -> cb.equal(r.get("pickupLocationId"), v);
    }
}

