package com.dvk.ct250backend.domain.flight.dto.request;

import com.dvk.ct250backend.domain.common.annotation.NotEmptyCollection;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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

    String arrivalDate;

    @NotEmptyCollection(message = "Passenger type quantity is required")
    List<PassengerTypeQuantityRequest> passengerTypeQuantityRequests;
}
