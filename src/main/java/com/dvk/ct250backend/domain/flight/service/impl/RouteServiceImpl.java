package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.RouteDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.domain.flight.entity.Route;
import com.dvk.ct250backend.domain.flight.mapper.RouteMapper;
import com.dvk.ct250backend.domain.flight.repository.AirportRepository;
import com.dvk.ct250backend.domain.flight.repository.RouteRepository;
import com.dvk.ct250backend.domain.flight.service.RouteService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    AirportRepository airportRepository;

    @Override
    @Transactional
    public RouteDTO createRoute(RouteDTO routeDTO) throws ResourceNotFoundException {
        Airport departureAirport = airportRepository.findById(routeDTO.getDepartureAirport().getAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Departure airport not found"));
        Airport arrivalAirport = airportRepository.findById(routeDTO.getArrivalAirport().getAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found"));
        Route route = routeMapper.toRoute(routeDTO);
        route.setDepartureAirport(departureAirport);
        route.setArrivalAirport(arrivalAirport);
        return routeMapper.toRouteDTO(routeRepository.save(route));
    }

    @Override
    public void deleteRoute(Integer id) throws ResourceNotFoundException {
        Route route = routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        routeRepository.delete(route);
    }

    @Override
    @Transactional
    public RouteDTO updateRoute(Integer id, RouteDTO routeDTO) throws ResourceNotFoundException {
        Route route = routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        routeMapper.updateRouteFromDTO(route, routeDTO);
        return routeMapper.toRouteDTO(routeRepository.save(route));
    }

    @Override
    public Page<RouteDTO> getRoutes(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Route> spec = getRouteSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Route> routePage = routeRepository.findAll(spec, pageable);
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

    private Specification<Route> getRouteSpec(Map<String, String> params) {
        Specification<Route> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = params.get("query");
            spec = spec.and((root, query, criteriaBuilder) -> {
                        Join<Route, Airport> departureAirport = root.join("departureAirport");
                        return criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(departureAirport.get("airportName"))),
                                                "%" + searchValue.toLowerCase() + "%"),
                                criteriaBuilder.like(departureAirport.get("airportCode"), "%" + searchValue.toUpperCase() + "%"));
                    }

            );
            spec = spec.or((root, query, criteriaBuilder) -> {
                Join<Route, Airport> arrivalAirport = root.join("arrivalAirport");
                return criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(arrivalAirport.get("airportName"))),
                                "%" + searchValue.toLowerCase() + "%"),
                        criteriaBuilder.like(arrivalAirport.get("airportCode"), "%" + searchValue.toUpperCase() + "%"));
            });
        }
        return spec;
    }

    @Override
    @Cacheable(value = "routes")
    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(routeMapper::toRouteDTO)
                .collect(Collectors.toList());
    }
}
