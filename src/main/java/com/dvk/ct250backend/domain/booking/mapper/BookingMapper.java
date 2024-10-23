package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDTO toBookingDTO(Booking booking);
    Booking toBooking(BookingDTO bookingDTO);
    void updateBookingFromDTO(@MappingTarget Booking booking, BookingDTO bookingDTO);
}
