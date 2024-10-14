package com.dvk.ct250backend.api;


import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.dto.FlightOverview;
import com.dvk.ct250backend.domain.flight.dto.request.FlightSearchRequest;
import com.dvk.ct250backend.domain.flight.service.FlightService;
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

    @GetMapping
    public ApiResponse<Page<FlightDTO>> getFlights(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<FlightDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(flightService.getFlights(params))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<FlightDTO> updateFlight(@PathVariable("id") Integer id,@RequestBody FlightDTO flightDTO) throws ResourceNotFoundException {
        return ApiResponse.<FlightDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(flightService.updateFlight(id, flightDTO))
                .build();
    }

    @PostMapping("/upload")
    public ApiResponse<String> uploadFlights(@RequestParam("files") List<MultipartFile> files) throws IOException {
        flightService.uploadFlights(files);
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .payload("Flights uploaded successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<FlightDTO>> searchFlights(@ModelAttribute FlightSearchRequest flightSearchRequest) {
        return ApiResponse.<List<FlightDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(flightService.searchFlights(flightSearchRequest))
                .build();
    }

    @GetMapping("/overview")
    public ApiResponse<List<FlightOverview>> getFlightOverview(@RequestParam("startDate") String startDate,
                                                          @RequestParam("endDate") String endDate) {
        return ApiResponse.<List<FlightOverview>>builder()
                .status(HttpStatus.OK.value())
                .payload(flightService.getFlightOverview(startDate, endDate))
                .build();
    }

}
