package com.dvk.ct250backend.domain.country.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import com.dvk.ct250backend.domain.country.entity.Country;
import com.dvk.ct250backend.domain.country.mapper.CountryMapper;
import com.dvk.ct250backend.domain.country.repository.CountryRepository;
import com.dvk.ct250backend.domain.country.service.CountryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryServiceImpl implements CountryService {

    CountryRepository countryRepository;
    CountryMapper countryMapper;

    @Override
    @Cacheable(value = "countries")
    public List<CountryDTO> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(countryMapper::toCountryDTO)
                .collect(Collectors.toList());

    }

    @Override
    public CountryDTO getCountry(Integer id) throws ResourceNotFoundException {
        Country country =  countryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Country not found with id: " + id)
        );
        return countryMapper.toCountryDTO(country);
    }
}
