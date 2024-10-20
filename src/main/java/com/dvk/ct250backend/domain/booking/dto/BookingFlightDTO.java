package com.dvk.ct250backend.domain.booking.dto;

import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingFlightDTO {
    Integer bookingFlightId;

    @NotBlank(message = "Flight is required")
    FlightDTO flight;

}
