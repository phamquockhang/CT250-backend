package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirplaneDTO;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.service.AirplaneService;
import com.dvk.ct250backend.domain.flight.service.AirportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/airplanes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AirplaneController {
    AirplaneService airplaneService;

    @GetMapping
    public ApiResponse<Page<AirplaneDTO>> getAirports(
            @RequestParam Map<String, String> params
    ) {
        return ApiResponse.<Page<AirplaneDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(airplaneService.getAirplanes(params))
                .build();
    }

    @GetMapping("/all")
  public ApiResponse<List<AirplaneDTO>> getAllAirplanes() {
    return ApiResponse.<List<AirplaneDTO>>builder()
        .status(HttpStatus.OK.value())
        .payload(airplaneService.getAllAirplane())
        .build();
  }

    @PostMapping
    public ApiResponse<AirplaneDTO> createAirplane(@RequestBody AirplaneDTO airplaneDTO) {
        return ApiResponse.<AirplaneDTO>builder()
            .status(HttpStatus.CREATED.value())
            .payload(airplaneService.createAirplane(airplaneDTO))
            .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAirplane(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        airplaneService.deleteAirplane(id);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AirplaneDTO> updateAirplane(@PathVariable("id") Integer id, @RequestBody AirplaneDTO airplaneDTO) throws ResourceNotFoundException {
        return ApiResponse.<AirplaneDTO>builder()
            .status(HttpStatus.OK.value())
            .payload(airplaneService.updateAirplane(id, airplaneDTO))
            .build();
    }
}
