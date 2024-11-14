package com.dvk.ct250backend.domain.booking.dto;

import com.dvk.ct250backend.domain.flight.dto.SeatDTO;
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
public class BookingPassengerDTO {
    Integer bookingPassengerId;
    @NotBlank(message = "Passenger is required")
    PassengerDTO passenger;
    BaggageDTO baggage;
    List<MealDTO> meals;
    List<SpecialServiceDTO> specialServices;
    SeatDTO seat;
    Boolean isPrimaryContact;
    Boolean isSharedSeat;
    String passengerGroup;
}
