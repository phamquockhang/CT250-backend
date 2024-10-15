package com.dvk.ct250backend.domain.flight.dto;

import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirportDTO {

    Integer airportId;

    @NotBlank(message = "Airport name is required")
    String airportName;

    @NotBlank(message = "Airport code is required")
    String airportCode;

    @NotBlank(message = "City name is required")
    String cityName;

    @NotBlank(message = "City code is required")
    String cityCode;

    String imgUrl;

    CountryDTO country;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    String createdBy;
    String updatedBy;
}
