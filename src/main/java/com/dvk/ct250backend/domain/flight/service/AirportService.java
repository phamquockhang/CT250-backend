package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import org.springframework.data.jpa.domain.Specification;



public interface AirportService {
    Page<AirportDTO> getAllAirport(Specification<Airport> spec, int page, int pageSize, String sort);
    AirportDTO createAirport(AirportDTO airportDTO);
    void deleteAirport(Integer id) throws ResourceNotFoundException;
    AirportDTO updateAirport(Integer id, AirportDTO airportDTO) throws ResourceNotFoundException;
}
