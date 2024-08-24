package com.dvk.ct250backend.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryDTO {
    Integer countryId;
    String countryName;
    Integer countryCode;
}
