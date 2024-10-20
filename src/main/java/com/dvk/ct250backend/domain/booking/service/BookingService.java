package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.dto.FlightSelectionDTO;

public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO) throws ResourceNotFoundException;
    Double calculateTotalPrice(FlightSelectionDTO flightSelectionDTO);
}
