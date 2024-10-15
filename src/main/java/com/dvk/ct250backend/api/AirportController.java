package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.service.AirportService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AirportController {

    AirportService airportService;

    @GetMapping
    public ApiResponse<Page<AirportDTO>> getAirports(
            @RequestParam Map<String, String> params
            ) {
        return ApiResponse.<Page<AirportDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(airportService.getAirports(params))
                .build();
    }


    @PostMapping
    public ApiResponse<AirportDTO> createAirport(@ModelAttribute  AirportDTO airportDTO, @RequestParam MultipartFile cityImg) throws IOException {
        return ApiResponse.<AirportDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(airportService.createAirport(airportDTO, cityImg))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAirport(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        airportService.deleteAirport(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AirportDTO> updateAirport(@PathVariable("id") Integer id, @Valid @RequestBody AirportDTO airportDTO) throws ResourceNotFoundException {
        return ApiResponse.<AirportDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(airportService.updateAirport(id, airportDTO))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<AirportDTO>> getAllAirports() {
        return ApiResponse.<List<AirportDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(airportService.getAllAirports())
                .build();
    }
}
