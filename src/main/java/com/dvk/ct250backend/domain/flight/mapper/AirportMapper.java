package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.country.mapper.CountryMapper;
import com.dvk.ct250backend.domain.flight.dto.AirportDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.infrastructure.elasticsearch.document.SearchAirportDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

//    @Mapping(source = "country.countryId", target = "countryId")
//    @Mapping(source = "country.countryName", target = "countryName")
//    @Mapping(source = "country.countryCode", target = "countryCode")
//    @Mapping(source = "country.iso2Code", target = "iso2Code")
//    @Mapping(source = "country.iso3Code", target = "iso3Code")
//    SearchAirportDocument toSearchAirportDocument(AirportDTO airportDTO);


//    @Mapping(source = "country.countryId", target = "countryId")
//    @Mapping(source = "country.countryName", target = "countryName")
//    @Mapping(source = "country.countryCode", target = "countryCode")
//    @Mapping(source = "country.iso2Code", target = "iso2Code")
//    @Mapping(source = "country.iso3Code", target = "iso3Code")
//    SearchAirportDocument toSearchAirportDocument(Airport airport);

//    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "formatDateTime")
//    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "formatDateTime")
//    SearchAirportDocument toSearchAirportDocument(Airport airport);
//
//    @Named("formatDateTime")
//    static String formatDateTime(LocalDateTime dateTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        return dateTime.format(formatter);
//    }

}
