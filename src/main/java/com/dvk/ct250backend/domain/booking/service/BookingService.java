package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;

import java.util.Map;

public interface BookingService {
    BookingDTO createInitBooking(BookingDTO bookingDTO);
    BookingDTO reserveBooking(Integer bookingId ,BookingDTO bookingDTO) throws ResourceNotFoundException;
    Page<BookingDTO> getBookings(Map<String, String> params);
    void deleteBooking(Integer bookingId) throws ResourceNotFoundException;
    BookingDTO updateBooking(Integer bookingId, BookingDTO bookingDTO) throws ResourceNotFoundException;
}
