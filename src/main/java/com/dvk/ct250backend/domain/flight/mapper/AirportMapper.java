package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.country.mapper.CountryMapper;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.infrastructure.elasticsearch.document.AirportDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface AirportMapper {
    AirportDTO toAirportDTO(Airport airport);
    Airport toAirport(AirportDTO airportDTO);
    void updateAirportFromDTO(@MappingTarget Airport airport, AirportDTO airportDTO);
    AirportDTO toAirportDTO(AirportDocument airportDocument);
}
