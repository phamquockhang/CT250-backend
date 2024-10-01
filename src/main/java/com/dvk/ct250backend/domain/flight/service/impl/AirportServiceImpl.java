package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.domain.flight.mapper.AirportMapper;
import com.dvk.ct250backend.domain.flight.repository.AirportRepository;
import com.dvk.ct250backend.domain.flight.service.AirportService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
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
public class AirportServiceImpl implements AirportService {

    AirportRepository airportRepository;
    AirportMapper airportMapper;
    RequestParamUtils requestParamUtils;


    @Override
    @Cacheable(value = "airports")
    public Page<AirportDTO> getAirports(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));

        Specification<Airport> spec = getAirportSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Airport> airportPage = airportRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(airportPage.getTotalPages())
                .total(airportPage.getTotalElements())
                .build();
        return Page.<AirportDTO>builder()
                .meta(meta)
                .content(airportPage.getContent().stream()
                        .map(airportMapper::toAirportDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Airport> getAirportSpec(Map<String, String> params) {
        Specification<Airport> spec = Specification.where(null);
        if(params.containsKey("query")){
            String searchValue = params.get("query");
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("airportName")), "%" + searchValue.toLowerCase() + "%"),
                    criteriaBuilder.like(root.get("airportCode"), "%" + params.get("query").toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("cityName")), "%" + searchValue.toLowerCase() + "%"),
                    criteriaBuilder.like(root.get("cityCode"), "%" + params.get("query").toUpperCase() + "%")
            ));
        }
        return spec;
    }

    @Override
    @Transactional
    @CacheEvict(value = "airports", allEntries = true)
    public AirportDTO createAirport(AirportDTO airportDTO) {
        Airport airport = airportMapper.toAirport(airportDTO);
        return airportMapper.toAirportDTO(airportRepository.save(airport));
    }

    @Override
    @CacheEvict(value = "airports", allEntries = true)
    public void deleteAirport(Integer id) throws ResourceNotFoundException {
        Airport airport = airportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
        airportRepository.delete(airport);
    }

    @Override
    @CacheEvict(value = "airports", allEntries = true)
    public AirportDTO updateAirport(Integer id, AirportDTO airportDTO) throws ResourceNotFoundException {
        Airport airport = airportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
        airportMapper.updateAirportFromDTO(airport, airportDTO);
        return airportMapper.toAirportDTO(airportRepository.save(airport));
    }

    @Override
    @Cacheable(value = "airports")
    public List<AirportDTO> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(airportMapper::toAirportDTO)
                .collect(Collectors.toList());
    }


}
