package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import org.springframework.data.jpa.domain.Specification;
import java.util.Map;


public interface AirportService {
    AirportDTO createAirport(AirportDTO airportDTO);
    void deleteAirport(Integer id) throws ResourceNotFoundException;
    AirportDTO updateAirport(Integer id, AirportDTO airportDTO) throws ResourceNotFoundException;
    Page<AirportDTO> getAirports(Map<String, String> params);
}
