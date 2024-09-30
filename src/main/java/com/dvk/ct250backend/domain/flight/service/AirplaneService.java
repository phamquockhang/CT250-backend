package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirplaneDTO;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;

import java.util.List;
import java.util.Map;

public interface AirplaneService {
    AirplaneDTO createAirplane(AirplaneDTO airplaneDTO);
    void deleteAirplane(Integer id) throws ResourceNotFoundException;
    AirplaneDTO updateAirplane(Integer id, AirplaneDTO airplaneDTO) throws ResourceNotFoundException;
    Page<AirplaneDTO> getAirplanes(Map<String, String> params);
    List<AirplaneDTO> getAllAirplane();
}
