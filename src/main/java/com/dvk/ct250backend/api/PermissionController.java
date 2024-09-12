package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.Page;
import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.service.PermissionService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ApiResponse<Page<PermissionDTO>> getAllPermission(
            @Filter Specification<Permission> spec,
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int pageSize) {
        return ApiResponse.<Page<PermissionDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(permissionService.getAllPermissions(spec, page, pageSize))
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) throws IdInValidException {
        return ApiResponse.<PermissionDTO>builder()
                .status(HttpStatus.CREATED.value())
                .data(permissionService.createPermission(permissionDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable("id") Long id) throws IdInValidException {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping
    public ApiResponse<PermissionDTO> updatePermission(@Valid @RequestBody PermissionDTO permissionDTO) throws IdInValidException {
        return ApiResponse.<PermissionDTO>builder()
                .status(HttpStatus.OK.value())
                .data(permissionService.updatePermission(permissionDTO))
                .build();
    }
}
