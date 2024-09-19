package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
import com.dvk.ct250backend.domain.auth.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ApiResponse<Page<RoleDTO>> getRole(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<RoleDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.getRoles(params))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<RoleDTO>> getAllRoles() {
        return ApiResponse.<List<RoleDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.getAllRoles())
                .build();
    }


    @PostMapping
    public ApiResponse<RoleDTO> create(@Valid @RequestBody RoleDTO roleDTO) throws ResourceNotFoundException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(roleService.createRole(roleDTO))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDTO> getRole(@PathVariable("id") Long id) throws ResourceNotFoundException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.getRoleById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable("id") Long id) throws ResourceNotFoundException {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping({"/{id}"})
    public ApiResponse<RoleDTO> updateRole(@PathVariable Long id,@Valid @RequestBody RoleDTO roleDTO) throws ResourceNotFoundException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.updateRole(id, roleDTO))
                .build();
    }


}
