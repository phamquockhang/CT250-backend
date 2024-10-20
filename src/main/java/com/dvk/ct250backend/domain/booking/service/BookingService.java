package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.domain.booking.dto.BookingDTO;

public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO);
    BookingDTO holdBooking(Long bookingId) ;
}
