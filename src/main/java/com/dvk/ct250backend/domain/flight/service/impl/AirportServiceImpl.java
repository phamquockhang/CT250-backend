package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.country.entity.Country;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.domain.flight.mapper.AirportMapper;
import com.dvk.ct250backend.domain.flight.repository.AirportRepository;
import com.dvk.ct250backend.domain.flight.service.AirportService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
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

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AirportServiceImpl implements AirportService {

    AirportRepository airportRepository;
    AirportMapper airportMapper;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;

//    @Override
//    @Cacheable(value = "airports")
//    public Page<AirportDTO> getAirports(Map<String, String> params) {
//        int page = Integer.parseInt(params.getOrDefault("page", "1"));
//        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
//
//        List<Sort.Order> sortOrders = requestParamUtils.toSortOrdersByElastic(params);
//        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(sortOrders));
//
//        String query = params.getOrDefault("query", "").toUpperCase();
//         org.springframework.data.domain.Page<SearchAirportDocument> airportPage;
//
//        if (query.isEmpty()) {
//            airportPage = airportElasticsearchRepository.findAll(pageable);
//        } else {
//            airportPage = airportElasticsearchRepository.findAll(query, pageable);
//        }
//
//        Meta meta = Meta.builder()
//                .page(pageable.getPageNumber() + 1)
//                .pageSize(pageable.getPageSize())
//                .pages(airportPage.getTotalPages())
//                .total(airportPage.getTotalElements())
//                .build();
//
//        return Page.<AirportDTO>builder()
//                .meta(meta)
//                .content(airportPage.getContent().stream()
//                        .map(airportMapper::toAirportDTO)
//                        .collect(Collectors.toList()))
//                .build();
//    }

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

//    private Specification<Airport> getAirportSpec(Map<String, String> params) {
//        Specification<Airport> spec = Specification.where(null);
//        if(params.containsKey("query")){
//            String searchValue = params.get("query");
//            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
//                    criteriaBuilder.like(criteriaBuilder.lower(root.get("airportName")), "%" + searchValue.toLowerCase() + "%"),
//                    criteriaBuilder.like(root.get("airportCode"), "%" + params.get("query").toUpperCase() + "%"),
//                    criteriaBuilder.like(criteriaBuilder.lower(root.get("cityName")), "%" + searchValue.toLowerCase() + "%"),
//                    criteriaBuilder.like(root.get("cityCode"), "%" + params.get("query").toUpperCase() + "%")
//            ));
//        }
//        return spec;
//    }


    private Specification<Airport> getAirportSpec(Map<String, String> params) {
        Specification<Airport> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = params.get("query").trim().toLowerCase();
            String[] searchValues = searchValue.split(",");
            spec = spec.or((root, query, criteriaBuilder) -> {
                Join<Airport, Country> countryJoin = root.join("country", JoinType.LEFT);
                return criteriaBuilder.or(
                        Arrays.stream(searchValues)
                                .map(stringUtils::normalizeString)
                                .map(value -> "%" + value.trim().toLowerCase() + "%")
                                .map(likePattern -> criteriaBuilder.or(
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("airportName")), likePattern),
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("airportCode")), likePattern),
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("cityName")), likePattern),
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("cityCode")), likePattern),
                                        criteriaBuilder.like(criteriaBuilder.lower(countryJoin.get("countryName")), likePattern)
                                ))
                                .toArray(Predicate[]::new)
                );
            });
        }
        return spec;
    }


    @Override
    @Transactional
    @CacheEvict(value = "airports", allEntries = true)
    public AirportDTO createAirport(AirportDTO airportDTO) {
        Airport airport = airportMapper.toAirport(airportDTO);
        airport = airportRepository.save(airport);
        return airportMapper.toAirportDTO(airport);
    }

    @Override
    @CacheEvict(value = "airports", allEntries = true)
    public void deleteAirport(Integer id) throws ResourceNotFoundException {
        Airport airport = airportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
        airportRepository.delete(airport);
    }

    @Override
    @CacheEvict(value = "airports", allEntries = true)
    @Transactional
    public AirportDTO updateAirport(Integer id, AirportDTO airportDTO) throws ResourceNotFoundException {
        Airport airport = airportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
        airportMapper.updateAirportFromDTO(airport, airportDTO);
        airport = airportRepository.save(airport);
        return airportMapper.toAirportDTO(airport);
    }

    @Override
    @Cacheable(value = "airports")
    public List<AirportDTO> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(airportMapper::toAirportDTO)
                .collect(Collectors.toList());
    }

}