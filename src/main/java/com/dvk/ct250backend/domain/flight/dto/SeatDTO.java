package com.dvk.ct250backend.domain.flight.dto;

import com.dvk.ct250backend.domain.flight.entity.Airplane;
import com.dvk.ct250backend.domain.flight.entity.SeatAvailability;
import com.dvk.ct250backend.domain.flight.enums.SeatClassEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
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
public class SeatDTO {
   Integer seatId;

    String seatClass;

    String seatCode;

//    List<SeatAvailabilityDTO> seatAvailability;
//
//    AirplaneDTO airplane;
}
