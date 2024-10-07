package com.dvk.ct250backend.domain.flight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightDTO {
    String flightId;

    @NotBlank(message = "Departure date time is required")
    LocalDateTime departureDateTime;

    @NotBlank(message = "Arrival date time is required")
    LocalDateTime arrivalDateTime;

    String flightStatus;

    RouteDTO route;

    List<FlightPricingDTO> flightPricing;
    List<SeatAvailabilityDTO> seatAvailability;

    AirplaneDTO airplane;


    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
