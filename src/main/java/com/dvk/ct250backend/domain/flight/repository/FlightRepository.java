package com.dvk.ct250backend.domain.flight.repository;

import com.dvk.ct250backend.domain.flight.entity.Flight;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FlightRepository extends JpaRepository<Flight, String>, JpaSpecificationExecutor<Flight> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM Flight f WHERE f.flightId = :flightId")
    Flight findFlightForUpdate(@Param("flightId") String flightId);
}
