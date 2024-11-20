package com.dvk.ct250backend.domain.country.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.country.dto.CountryDTO;

import java.util.List;

public interface CountryService {
    List<CountryDTO> getAllCountries();
    CountryDTO getCountry(Integer id) throws ResourceNotFoundException;
}
