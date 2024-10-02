package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.domain.flight.dto.FlightDTO;

import java.util.List;

public interface FlightService {
    List<FlightDTO> getAllFlights();
}
