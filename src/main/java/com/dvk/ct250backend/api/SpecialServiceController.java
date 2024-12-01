package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.SpecialServiceDTO;
import com.dvk.ct250backend.domain.booking.service.SpecialServiceInterface;
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
@RequestMapping("/api/v1/special-services")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecialServiceController {
    SpecialServiceInterface specialServiceInterface;

    @PostMapping
    public ApiResponse<SpecialServiceDTO> createSpecialService(@ModelAttribute SpecialServiceDTO specialServiceDTO, @RequestParam MultipartFile specialServiceImg) throws IOException {
        return ApiResponse.<SpecialServiceDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(specialServiceInterface.createSpecialService(specialServiceDTO, specialServiceImg))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SpecialServiceDTO> updateSpecialService(@PathVariable("id") Integer specialServiceId, @ModelAttribute SpecialServiceDTO specialServiceDTO, @RequestParam(required = false) MultipartFile specialServiceImg) throws IOException, ResourceNotFoundException {
        return ApiResponse.<SpecialServiceDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(specialServiceInterface.updateSpecialService(specialServiceId, specialServiceDTO, specialServiceImg))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSpecialService(@PathVariable("id") Integer specialServiceId) throws ResourceNotFoundException {
        specialServiceInterface.deleteSpecialService(specialServiceId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @GetMapping
    public ApiResponse<Page<SpecialServiceDTO>> getSpecialServices(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<SpecialServiceDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(specialServiceInterface.getSpecialServices(params))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<SpecialServiceDTO>> getAllSpecialService() {
        return ApiResponse.<List<SpecialServiceDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(specialServiceInterface.getAllSpecialServices())
                .build();
    }
}
