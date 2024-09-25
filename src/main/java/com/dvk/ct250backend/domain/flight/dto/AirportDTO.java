package com.dvk.ct250backend.domain.flight.dto;

import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirportDTO {

    Integer airportId;

    String airportName;

    String airportCode;

    String city;

    CountryDTO country;
}
