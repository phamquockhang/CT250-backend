package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.domain.flight.mapper.AirportMapper;
import com.dvk.ct250backend.domain.flight.repository.AirportRepository;
import com.dvk.ct250backend.domain.flight.service.AirportService;
import com.dvk.ct250backend.infrastructure.elasticsearch.document.SearchAirportDocument;
import com.dvk.ct250backend.infrastructure.elasticsearch.repository.AirportElasticRepo;
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
public class AirportServiceImpl implements AirportService {

    AirportRepository airportRepository;
    AirportMapper airportMapper;
    RequestParamUtils requestParamUtils;
    AirportElasticRepo airportElasticsearchRepository;

    @Override
    @Cacheable(value = "airports", key = "#params['query'] + '-' + #params['page'] + '-' + #params['pageSize']")
    public Page<AirportDTO> getAirports(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));

        List<Sort.Order> sortOrders = requestParamUtils.toSortOrdersByElastic(params);
        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(sortOrders));

        String query = params.getOrDefault("query", "").toUpperCase();
         org.springframework.data.domain.Page<SearchAirportDocument> airportPage;

        if (query.isEmpty()) {
            airportPage = airportElasticsearchRepository.findAll(pageable);
        } else {
            airportPage = airportElasticsearchRepository.findAll(query, pageable);
           // String[] terms = query.split(" ");
            //airportPage = airportElasticsearchRepository.findAll(String.join(" OR ", terms), pageable);
        }

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