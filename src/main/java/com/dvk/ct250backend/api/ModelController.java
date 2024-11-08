package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.domain.flight.dto.ModelDTO;
import com.dvk.ct250backend.domain.flight.service.ModelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/models")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelController {
    ModelService modelService;

    @GetMapping("/all")
    public ApiResponse<List<ModelDTO>> getAllModels() {
        return ApiResponse.<List<ModelDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(modelService.getAllModel())
                .build();
    }

    @PostMapping
    public ApiResponse<ModelDTO> createModel(@RequestBody ModelDTO modelDTO) {
        return ApiResponse.<ModelDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(modelService.createModel(modelDTO))
                .build();
    }
}
