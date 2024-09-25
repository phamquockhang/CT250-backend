package com.dvk.ct250backend.domain.country.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryDTO {
    Integer countryId;
    String countryName;
    Integer countryCode;
    String iso2Code;
    String iso3Code;


}
