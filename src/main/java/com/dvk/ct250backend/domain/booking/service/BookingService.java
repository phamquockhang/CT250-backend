package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public interface BookingService {
    BookingDTO createInitBooking(BookingDTO bookingDTO);
    BookingDTO reserveBooking(Integer bookingId ,BookingDTO bookingDTO) throws ResourceNotFoundException;
    Page<BookingDTO> getBookings(Map<String, String> params);
    String searchBooking(String code, HttpServletResponse response) throws ResourceNotFoundException, IOException;
}
