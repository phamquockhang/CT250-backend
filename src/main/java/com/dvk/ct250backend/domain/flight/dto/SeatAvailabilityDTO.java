package com.dvk.ct250backend.domain.flight.dto;

import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.entity.Seat;
import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatAvailabilityDTO {
    Integer seatAvailabilityId;

    Integer totalSeats;
    Integer bookedSeats;
    //Flight flight;
    SeatDTO seat;

   String status;

    String position;

}
