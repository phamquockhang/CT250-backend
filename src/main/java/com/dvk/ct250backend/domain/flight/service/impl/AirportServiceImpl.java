package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.mapper.AirportMapper;
import com.dvk.ct250backend.domain.flight.repository.AirportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AirportServiceImpl implements AirportService {
    AirportRepository airportRepository;
    AirportMapper airportMapper;


    @Override
    @Cacheable(value = "airports")
    public List<AirportDTO> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(airportMapper::toAirportDTO)
                .toList();
    }


}
