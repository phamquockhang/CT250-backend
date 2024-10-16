package com.dvk.ct250backend.domain.flight.repository;

import com.dvk.ct250backend.domain.flight.entity.SeatAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatAvailabilityRepository extends JpaRepository<SeatAvailability, Integer> {
}
