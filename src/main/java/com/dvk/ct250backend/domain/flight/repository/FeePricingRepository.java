package com.dvk.ct250backend.domain.flight.repository;

import com.dvk.ct250backend.domain.flight.entity.FeePricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeePricingRepository extends JpaRepository<FeePricing, Integer> {
}
