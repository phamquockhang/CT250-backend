package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;
import com.dvk.ct250backend.domain.auth.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ApiResponse<Page<PermissionDTO>> getAllPermission(
            @RequestParam Map<String, String> params
    ){
        return ApiResponse.<Page<PermissionDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(permissionService.getAllPermissions(params))
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) throws ResourceNotFoundException {
        return ApiResponse.<PermissionDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(permissionService.createPermission(permissionDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable("id") Long id) throws ResourceNotFoundException {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping({"/{id}"})
    public ApiResponse<PermissionDTO> updatePermission(@PathVariable("id") Long id ,@Valid @RequestBody PermissionDTO permissionDTO) throws ResourceNotFoundException {
        return ApiResponse.<PermissionDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(permissionService.updatePermission(id, permissionDTO))
                .build();
    }
}
