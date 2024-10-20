package com.dvk.ct250backend.domain.flight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    AirportDTO departureAirport;

    AirportDTO arrivalAirport;

    @NotBlank(message = "Route type is required")
    String routeType;

    @Min(value = 1, message = "Duration must be greater than 0")
    @NotNull(message = "Duration is required")
    Integer duration;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
