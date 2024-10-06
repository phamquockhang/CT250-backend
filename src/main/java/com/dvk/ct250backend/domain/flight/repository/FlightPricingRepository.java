package com.dvk.ct250backend.domain.flight.repository;

import com.dvk.ct250backend.domain.flight.entity.FlightPricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightPricingRepository extends JpaRepository<FlightPricing, Integer> {
}
