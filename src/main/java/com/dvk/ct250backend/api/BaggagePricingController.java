package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BaggagePricingDTO;
import com.dvk.ct250backend.domain.booking.service.BaggagePricingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/baggage-pricing")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BaggagePricingController {

    BaggagePricingService baggagePricingService;

    @GetMapping("/all")
    public ApiResponse<List<BaggagePricingDTO>> getAllBaggagePricing() {
        return ApiResponse.<List<BaggagePricingDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(baggagePricingService.getAllBaggagePricings())
                .build();
    }

    @PostMapping
    public ApiResponse<BaggagePricingDTO> createBaggagePricing(@RequestBody BaggagePricingDTO baggagePricingDTO){
        return ApiResponse.<BaggagePricingDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(baggagePricingService.createBaggagePricing(baggagePricingDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BaggagePricingDTO> updateBaggagePricing(@PathVariable("id") Integer baggagePricingId, @RequestBody BaggagePricingDTO baggagePricingDTO) throws ResourceNotFoundException {
        return ApiResponse.<BaggagePricingDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(baggagePricingService.updateBaggagePricing(baggagePricingId, baggagePricingDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBaggagePricing(@PathVariable("id") Integer baggagePricingId) throws ResourceNotFoundException {
        baggagePricingService.deleteBaggagePricing(baggagePricingId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }
}
