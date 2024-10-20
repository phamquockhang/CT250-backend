package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.PassengerDTO;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    PassengerDTO toPassengerDTO(Passenger passenger);
    Passenger toPassenger(PassengerDTO passengerDTO);

}
