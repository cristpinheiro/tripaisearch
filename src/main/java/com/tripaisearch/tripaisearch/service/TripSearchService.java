package com.tripaisearch.tripaisearch.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tripaisearch.tripaisearch.entity.Trip;
import com.tripaisearch.tripaisearch.filters.AiFilters;
import com.tripaisearch.tripaisearch.filters.TripSpecs;
import com.tripaisearch.tripaisearch.repository.TripRepository;

@Service
public final class TripSearchService {

    private final TripRepository repo;

    public TripSearchService(TripRepository repo) {
        this.repo = repo;
    }

    public List<Trip> search(AiFilters f) {
        Specification<Trip> s = Specification.allOf();
        if (f.start() != null)
            s = s.and(TripSpecs.start(f.start()));
        if (f.end() != null)
            s = s.and(TripSpecs.end(f.end()));
        if (f.minPassengers() != null)
            s = s.and(TripSpecs.minPassengers(f.minPassengers()));
        if (f.pickupZone() != null)
            s = s.and(TripSpecs.pickupZone(f.pickupZone()));
        return repo.findAll(s);
    }

}
