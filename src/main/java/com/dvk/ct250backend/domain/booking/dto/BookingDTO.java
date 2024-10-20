package com.dvk.ct250backend.domain.booking.dto;

import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {
    Integer bookingId;

    @NotBlank(message = "Trip type is required")
    String tripType;

    @NotBlank(message = "Departure flight is required")
    FlightDTO flight;

    @NotBlank(message = "Total price is required")
    Double totalPrice;

    @NotBlank(message = "Passengers are required")
    Set<PassengerDTO> passengers;

    @NotBlank(message = "Booking status is required")
    String bookingStatus;

    Set<BookingPriceDetailDTO> bookingPriceDetails;
}
