package com.dvk.ct250backend.domain.flight.dto;

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
public class RouteDTO {
    Integer routeId;
    @NotBlank(message = "Departure airport is required")
    AirportDTO departureAirport;

    @NotBlank(message = "Arrival airport is required")
    AirportDTO arrivalAirport;

    @NotBlank(message = "Route type is required")
    String routeType;

    @NotBlank(message = "Duration is required")
    String duration;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
