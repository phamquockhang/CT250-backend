package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.dto.FlightSelectionDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.mapper.BookingMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    FlightRepository flightRepository;
    BookingMapper bookingMapper;
    BookingRepository bookingRepository;

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        return bookingMapper.toBookingDTO(bookingRepository.save(booking));
    }

    @Override
    public Double calculateTotalPrice(FlightSelectionDTO flightSelectionDTO) {
        double totalPrice = 0.0;
        return null;
    }
}