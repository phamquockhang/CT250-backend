package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.entity.Flight;

import java.util.List;

public interface FlightService {
    List<FlightDTO> getAllFlights();

    FlightDTO updateFlight(Integer id, FlightDTO flightDTO) throws ResourceNotFoundException;
}
