package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.country.mapper.CountryMapper;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.infrastructure.elasticsearch.document.SearchAirportDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface AirportMapper {
    AirportDTO toAirportDTO(Airport airport);
    Airport toAirport(AirportDTO airportDTO);
    void updateAirportFromDTO(@MappingTarget Airport airport, AirportDTO airportDTO);

    @Mapping(source = "countryId", target = "country.countryId")
    @Mapping(source = "countryName", target = "country.countryName")
    @Mapping(source = "countryCode", target = "country.countryCode")
    @Mapping(source = "iso2Code", target = "country.iso2Code")
    @Mapping(source = "iso3Code", target = "country.iso3Code")
    AirportDTO toAirportDTO(SearchAirportDocument airportDocument);
}
