package com.dvk.ct250backend.domain.booking.dto;

import com.dvk.ct250backend.domain.booking.entity.Baggage;
import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {
    Integer bookingId;

    @NotBlank(message = "Trip type is required")
    String tripType;

    @NotBlank(message = "Booking flights are required")
    List<BookingFlightDTO> bookingFlights;

    @NotBlank(message = "Total price is required")
    BigDecimal totalPrice;

    @NotBlank
    List<BookingPassengerDTO> bookingPassengers;

    @NotBlank(message = "Booking status is required")
    String bookingStatus;
}
