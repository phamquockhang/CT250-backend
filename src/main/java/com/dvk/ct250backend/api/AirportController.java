package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.service.impl.AirportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AirportController {
    AirportService airportService;

    @GetMapping("/all")
    public ApiResponse<List<AirportDTO>> getAllAirports() {
        return ApiResponse.<List<AirportDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(airportService.getAllAirports())
                .build();
    }
}
