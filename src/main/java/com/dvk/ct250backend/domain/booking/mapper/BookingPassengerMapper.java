package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.BookingPassengerDTO;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingPassengerMapper {
    BookingPassengerDTO toBookingPassengerDTO(BookingPassenger bookingPassenger);
    BookingPassenger toBookingPassenger(BookingPassengerDTO bookingPassengerDTO);
}
