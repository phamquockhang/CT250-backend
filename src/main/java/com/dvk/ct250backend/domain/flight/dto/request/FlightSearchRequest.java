package com.dvk.ct250backend.domain.flight.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightSearchRequest {

    @NotBlank(message = "Arrival airport code is required")
    String arrivalLocation;

    @NotBlank(message = "Departure airport code is required")
    String departureLocation;

    @NotBlank(message = "Departure date is required")
    String departureDate;

    @NotBlank(message = "Return date is required")
    String arrivalDate;
}
