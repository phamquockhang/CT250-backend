package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BaggageDTO;
import com.dvk.ct250backend.domain.booking.service.BaggageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/baggage")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BaggageController {

    BaggageService baggageService;
    @GetMapping("/all")
    public ApiResponse<List<BaggageDTO>> getAllBaggage() {
        return ApiResponse.<List<BaggageDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(baggageService.getAllBaggage())
                .build();
    }

    @GetMapping
    public ApiResponse<Page<BaggageDTO>> getBaggage(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<BaggageDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(baggageService.getBaggage(params))
                .build();
    }

    @PostMapping
    public ApiResponse<BaggageDTO> createBaggage(@RequestBody BaggageDTO baggageDTO){
        return ApiResponse.<BaggageDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(baggageService.createBaggage(baggageDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BaggageDTO> updateBaggage(@PathVariable("id") Integer baggageId, @RequestBody BaggageDTO baggageDTO) throws ResourceNotFoundException, ResourceNotFoundException {
        return ApiResponse.<BaggageDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(baggageService.updateBaggage(baggageId, baggageDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBaggage(@PathVariable("id") Integer baggageId) throws ResourceNotFoundException {
        baggageService.deleteBaggage(baggageId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

}
