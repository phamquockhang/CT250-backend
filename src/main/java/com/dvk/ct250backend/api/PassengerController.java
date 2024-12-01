package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.PassengerDTO;
import com.dvk.ct250backend.domain.booking.service.PassengerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PassengerController {
    PassengerService passengerService;

    @GetMapping
    public ApiResponse<Page<PassengerDTO>> getPassengers(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<PassengerDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(passengerService.getPassengers(params))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PassengerDTO> updatePassenger(@PathVariable("id") Integer passengerId, @RequestBody PassengerDTO passengerDTO) throws ResourceNotFoundException {
        return ApiResponse.<PassengerDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(passengerService.updatePassenger(passengerId, passengerDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePassenger(@PathVariable("id") Integer passengerId) throws ResourceNotFoundException {
        passengerService.deletePassenger(passengerId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/share-stats")
    public ApiResponse<Map<String, Integer>> getPassengerShareStats() {
        return ApiResponse.<Map<String, Integer>>builder()
                .status(HttpStatus.OK.value())
                .payload(passengerService.getPassengerShareStats())
                .build();
    }
}
