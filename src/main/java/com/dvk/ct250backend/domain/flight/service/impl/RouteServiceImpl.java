package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.RouteDTO;
import com.dvk.ct250backend.domain.flight.entity.Route;
import com.dvk.ct250backend.domain.flight.mapper.RouteMapper;
import com.dvk.ct250backend.domain.flight.repository.RouteRepository;
import com.dvk.ct250backend.domain.flight.service.RouteService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RouteServiceImpl implements RouteService {
    RouteRepository routeRepository;
    RouteMapper routeMapper;
    RequestParamUtils requestParamUtils;

    @Override
    @Transactional
    @CacheEvict(value = "routes", allEntries = true)
    public RouteDTO createRoute(RouteDTO RouteDTO) {
        Route route = routeMapper.toRoute(RouteDTO);
        return routeMapper.toRouteDTO(routeRepository.save(route));
    }

    @Override
    @CacheEvict(value = "routes", allEntries = true)
    public void deleteRoute(Integer id) throws ResourceNotFoundException {
        Route route = routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        routeRepository.delete(route);
    }

    @Override
    @Transactional
    @CacheEvict(value = "routes", allEntries = true)
    public RouteDTO updateRoute(Integer id, RouteDTO RouteDTO) throws ResourceNotFoundException {
        Route route = routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        routeMapper.updateRouteFromDTO(route, RouteDTO);
        return routeMapper.toRouteDTO(routeRepository.save(route));
    }

    @Override
    @Cacheable(value = "routes")
    public Page<RouteDTO> getRoutes(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        //  Specification<Airplane> spec = getAirplaneSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params.getOrDefault("sort", ""));
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Route> routePage = routeRepository.findAll( pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(routePage.getTotalPages())
                .total(routePage.getTotalElements())
                .build();
        return Page.<RouteDTO>builder()
                .meta(meta)
                .content(routePage.getContent().stream()
                        .map(routeMapper::toRouteDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Cacheable(value = "routes")
    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(routeMapper::toRouteDTO)
                .toList();
    }
}
