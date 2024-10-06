package com.dvk.ct250backend.api;


import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.service.FlightService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlightController {

    FlightService flightService;

    @GetMapping("/all")
    public ApiResponse<List<FlightDTO>> getAllFlights() {
        return ApiResponse.<List<FlightDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(flightService.getAllFlights())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<FlightDTO> updateFlight(@PathVariable("id") Integer id,@RequestBody FlightDTO flightDTO) throws ResourceNotFoundException {
        return ApiResponse.<FlightDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(flightService.updateFlight(id, flightDTO))
                .build();
    }

}
