package com.dvk.ct250backend.domain.country.service;

import com.dvk.ct250backend.domain.country.dto.CountryDTO;

import java.util.List;

public interface CountryService {
    List<CountryDTO> getCountries();
}
