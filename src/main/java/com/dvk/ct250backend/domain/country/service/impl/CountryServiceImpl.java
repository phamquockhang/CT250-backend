package com.dvk.ct250backend.domain.country.service.impl;

import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import com.dvk.ct250backend.domain.country.entity.Country;
import com.dvk.ct250backend.domain.country.mapper.CountryMapper;
import com.dvk.ct250backend.domain.country.repository.CountryRepository;
import com.dvk.ct250backend.domain.country.service.CountryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryServiceImpl implements CountryService {

    CountryRepository countryRepository;
    CountryMapper countryMapper;

    @Override
    public List<CountryDTO> getCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(countryMapper::toCountryDTO)
                .toList();
    }

    @Override
    public Optional<Country> findById(Integer id) { // Implement this method
        return countryRepository.findById(id);
    }
}
