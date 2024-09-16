package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.service.RoleService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ApiResponse<Page<RoleDTO>> getAllRole(@Filter Specification<Role> spec,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(required = false) String sort) {
        return ApiResponse.<Page<RoleDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.getAllRoles(spec, page, pageSize, sort))
                .build();
    }


    @PostMapping
    public ApiResponse<RoleDTO> create(@Valid @RequestBody RoleDTO role) throws IdInValidException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(roleService.createRole(role))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDTO> getRole(@PathVariable("id") Long id) throws IdInValidException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.getRoleById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable("id") Long id) throws IdInValidException {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping
    public ApiResponse<RoleDTO> updateRole(@Valid @RequestBody RoleDTO role) throws IdInValidException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.updateRole(role))
                .build();
    }


}
