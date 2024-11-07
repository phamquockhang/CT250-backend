package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.domain.flight.dto.FeeDTO;
import com.dvk.ct250backend.domain.flight.service.FeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
