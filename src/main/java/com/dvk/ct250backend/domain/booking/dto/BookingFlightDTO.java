package com.dvk.ct250backend.domain.booking.dto;

import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.dto.TicketClassDTO;
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
public class BookingFlightDTO {
    Integer bookingFlightId;
    @NotBlank(message = "Flight is required")
    FlightDTO flight;
    @NotBlank(message = "Ticket class is required")
    TicketClassDTO ticketClass;
    List<BookingPassengerDTO> bookingPassengers;
}
