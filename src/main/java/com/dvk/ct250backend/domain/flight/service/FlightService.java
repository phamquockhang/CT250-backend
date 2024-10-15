package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.dto.FlightOverview;
import com.dvk.ct250backend.domain.flight.dto.request.FlightSearchRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FlightService {
    List<FlightDTO> getAllFlights();
    void uploadFlights(List<MultipartFile> files) throws IOException;
    FlightDTO updateFlight(String id, FlightDTO flightDTO) throws ResourceNotFoundException;
    List<FlightDTO> searchFlights(FlightSearchRequest flightSearchRequest);

    List<FlightOverview> getFlightOverview(String startDate, String endDate);

    Page<FlightDTO> getFlights(Map<String,String> params);

    FlightDTO getFlightById(String id) throws ResourceNotFoundException;
}
