package com.dvk.ct250backend.domain;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDTO toCountryDTO(Country country);
    Country toCountry(CountryDTO countryDTO);
}
