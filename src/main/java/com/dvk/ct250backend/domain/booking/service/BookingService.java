package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.dto.FlightSelectionDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;

public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO) throws ResourceNotFoundException;
    Double calculateTotalPrice(FlightSelectionDTO flightSelectionDTO);
}
