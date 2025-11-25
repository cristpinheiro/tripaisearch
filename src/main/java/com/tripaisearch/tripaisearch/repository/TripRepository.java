package com.tripaisearch.tripaisearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tripaisearch.tripaisearch.entity.Trip;

public interface TripRepository
    extends JpaRepository<Trip, Long>,
            JpaSpecificationExecutor<Trip> {}

