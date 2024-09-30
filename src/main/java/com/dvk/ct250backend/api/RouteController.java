package com.dvk.ct250backend.api;


import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.RouteDTO;
import com.dvk.ct250backend.domain.flight.service.RouteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RouteController {

    RouteService routeService;

    @GetMapping("/all")
    public ApiResponse<List<RouteDTO>> getAllRoutes() {
        return ApiResponse.<List<RouteDTO>>builder()
            .status(HttpStatus.OK.value())
            .payload(routeService.getAllRoutes())
            .build();
    }

    @GetMapping
    public ApiResponse<Page<RouteDTO>> getRoutes(
            @RequestParam Map<String, String> params
    ) {
        return ApiResponse.<Page<RouteDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(routeService.getRoutes(params))
                .build();
    }

    @PostMapping
    public ApiResponse<RouteDTO> createRoute(@RequestBody RouteDTO routeDTO) {
        return ApiResponse.<RouteDTO>builder()
            .status(HttpStatus.CREATED.value())
            .payload(routeService.createRoute(routeDTO))
            .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<RouteDTO> updateRoute(@PathVariable("id") Integer id, @RequestBody RouteDTO routeDTO) throws ResourceNotFoundException {
        return ApiResponse.<RouteDTO>builder()
            .status(HttpStatus.OK.value())
            .payload(routeService.updateRoute(id, routeDTO))
            .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRoute(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        routeService.deleteRoute(id);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .build();
    }
}
