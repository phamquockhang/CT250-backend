package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.domain.flight.dto.AirportDTO;

import java.util.List;


public interface AirportService {
    List<AirportDTO> getAllAirports();
}
