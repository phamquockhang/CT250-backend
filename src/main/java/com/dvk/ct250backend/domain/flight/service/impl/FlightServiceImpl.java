package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.mapper.FlightMapper;
import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
import com.dvk.ct250backend.domain.flight.service.FlightService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlightServiceImpl implements FlightService {

    FlightRepository flightRepository;
    FlightMapper flightMapper;

    @Override
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(flightMapper::toFlightDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FlightDTO updateFlight(Integer id, FlightDTO flightDTO) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found for this id :: " + id));
        flightMapper.updateFlightFromDTO(flight, flightDTO);
        return flightMapper.toFlightDTO(flightRepository.save(flight));
    }

}
