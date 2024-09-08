//package com.dvk.ct250backend.api;
//
//import com.dvk.ct250backend.app.dto.response.ApiResponse;
//import com.dvk.ct250backend.app.exception.IdInValidException;
//import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
//import com.dvk.ct250backend.domain.auth.service.RoleService;
//import jakarta.validation.Valid;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/roles")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class RoleController {
//    RoleService roleService;
//
//    @GetMapping
//    public ApiResponse<RoleDTO> getAll() {
//        return ApiResponse.<RoleDTO>builder()
//                .status(HttpStatus.OK.value())
//                .data(roleService.getAllRoles())
//                .build();
//    }
//
//    @PostMapping
//    public ApiResponse<RoleDTO> create(@Valid @RequestBody RoleDTO r) throws IdInValidException {
//        return ApiResponse.<RoleDTO>builder()
//                .status(HttpStatus.CREATED.value())
//                .data(roleService.createRole(r))
//                .build();
//    }
//}
