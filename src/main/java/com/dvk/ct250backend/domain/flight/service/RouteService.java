package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.RouteDTO;

import java.util.List;
import java.util.Map;

public interface RouteService {
    RouteDTO createRoute(RouteDTO RouteDTO);
    void deleteRoute(Integer id) throws ResourceNotFoundException;
    RouteDTO updateRoute(Integer id, RouteDTO RouteDTO) throws ResourceNotFoundException;
    Page<RouteDTO> getRoutes(Map<String, String> params);
    List<RouteDTO> getAllRoutes();
}
