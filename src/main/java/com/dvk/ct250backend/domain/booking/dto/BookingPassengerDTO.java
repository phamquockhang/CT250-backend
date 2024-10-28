package com.dvk.ct250backend.domain.booking.dto;

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
    Boolean isPrimaryContact;
    Boolean isSharedSeat;
}
