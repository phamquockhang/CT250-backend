package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import com.dvk.ct250backend.domain.country.service.CountryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryController {

    CountryService countryService;

    @GetMapping("/all")
    public ApiResponse<List<CountryDTO>> getAllCountries() {
        return ApiResponse.<List<CountryDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(countryService.getAllCountries())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CountryDTO> getCountryById(@PathVariable Integer id) throws ResourceNotFoundException {
        return ApiResponse.<CountryDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(countryService.getCountry(id))
                .build();
    }
}
