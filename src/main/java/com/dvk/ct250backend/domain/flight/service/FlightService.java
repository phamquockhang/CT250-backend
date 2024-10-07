package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FlightService {
    List<FlightDTO> getAllFlights();
    void uploadFlights(List<MultipartFile> files) throws IOException;
    FlightDTO updateFlight(Integer id, FlightDTO flightDTO) throws ResourceNotFoundException;
}
