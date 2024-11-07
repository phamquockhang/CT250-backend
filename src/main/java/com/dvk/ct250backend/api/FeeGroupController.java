package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.domain.flight.dto.FeeGroupDTO;
import com.dvk.ct250backend.domain.flight.service.FeeGroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                .status(200)
                .payload(feeGroupService.getAllFeeGroups())
                .build();
    }
}
