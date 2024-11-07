package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.domain.flight.dto.FeeGroupDTO;
import com.dvk.ct250backend.domain.flight.service.FeeGroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fee-groups")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeeGroupController {

    FeeGroupService feeGroupService;

    @GetMapping("/all")
    public ApiResponse<List<FeeGroupDTO>> getAllFeeGroups() {
        return ApiResponse.<List<FeeGroupDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(feeGroupService.getAllFeeGroups())
                .build();
    }

    @PostMapping
    public ApiResponse<FeeGroupDTO> createFeeGroup(@RequestBody FeeGroupDTO feeGroupDTO) {
        return ApiResponse.<FeeGroupDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(feeGroupService.createFeeGroup(feeGroupDTO))
                .build();
    }
}
