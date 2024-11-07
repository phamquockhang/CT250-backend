package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.domain.flight.dto.FeeDTO;
import com.dvk.ct250backend.domain.flight.service.FeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/fees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeeController {
    FeeService feeService;

    @GetMapping
    public ApiResponse<Page<FeeDTO>> getFees(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<FeeDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(feeService.getFees(params))
                .build();
    }

    @PostMapping
    public ApiResponse<FeeDTO> createFee(@RequestBody FeeDTO feeDTO) {
        return ApiResponse.<FeeDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(feeService.createFee(feeDTO))
                .build();
    }

}
