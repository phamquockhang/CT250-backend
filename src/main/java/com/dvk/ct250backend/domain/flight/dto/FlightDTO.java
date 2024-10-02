package com.dvk.ct250backend.domain.flight.dto;

import com.dvk.ct250backend.domain.flight.entity.Airplane;
import com.dvk.ct250backend.domain.flight.entity.FlightPricing;
import com.dvk.ct250backend.domain.flight.entity.Route;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightDTO {
    Integer flightId;

    @NotBlank(message = "Flight name is required")
    String flightName;

    @NotBlank(message = "Departure date time is required")
    LocalDateTime departureDateTime;

    @NotBlank(message = "Arrival date time is required")
    LocalDateTime arrivalDateTime;

    String flightDuration;

    RouteDTO route;

    @NotBlank()
    List<FlightPricingDTO> flightPricing;

    AirplaneDTO airplane;


    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
