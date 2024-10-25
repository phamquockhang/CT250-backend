package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import com.dvk.ct250backend.domain.booking.repository.BookingFlightRepository;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingFlightServiceImpl implements BookingFlightService {

    FlightRepository flightRepository;
    BookingFlightRepository bookingFlightRepository;

    @Override
    public void saveBookingFlight(BookingFlight bookingFlight) {
        Flight flight = flightRepository.findById(bookingFlight.getFlight().getFlightId()).orElseThrow();
        bookingFlight.setFlight(flight);
        bookingFlightRepository.save(bookingFlight);
    }
}
