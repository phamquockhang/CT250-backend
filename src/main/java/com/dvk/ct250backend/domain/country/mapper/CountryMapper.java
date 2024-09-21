package com.dvk.ct250backend.domain.country.mapper;

import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import com.dvk.ct250backend.domain.country.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDTO toCountryDTO(Country country);
    Country toCountry(CountryDTO countryDTO);
}
