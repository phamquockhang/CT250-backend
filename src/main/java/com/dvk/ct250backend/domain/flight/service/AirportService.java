package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface AirportService {
    AirportDTO createAirport(AirportDTO airportDTO, MultipartFile imgUrl) throws IOException;
    void deleteAirport(Integer id) throws ResourceNotFoundException;
    AirportDTO updateAirport(Integer id, AirportDTO airportDTO, MultipartFile imgUrl) throws ResourceNotFoundException, IOException;
    Page<AirportDTO> getAirports(Map<String, String> params);
    List<AirportDTO> getAllAirports();
}
