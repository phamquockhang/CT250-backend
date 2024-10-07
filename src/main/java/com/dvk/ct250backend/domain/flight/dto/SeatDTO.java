package com.dvk.ct250backend.domain.flight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatDTO {
   Integer seatId;

    String seatClass;

    String seatCode;

//    List<SeatAvailabilityDTO> seatAvailability;
//
//    AirplaneDTO airplane;
}
