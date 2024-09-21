package com.dvk.ct250backend.domain.country.service;

import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import com.dvk.ct250backend.domain.country.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    List<CountryDTO> getAllCountries();
    Optional<Country> findById(Integer id);
}
