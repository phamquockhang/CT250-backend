package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.BookingFlightDTO;
import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingFlightMapper {
    BookingFlightDTO toBookingFlightDTO(BookingFlight bookingFlight);
    BookingFlight toBookingFlight(BookingFlightDTO bookingFlightDTO);
}
