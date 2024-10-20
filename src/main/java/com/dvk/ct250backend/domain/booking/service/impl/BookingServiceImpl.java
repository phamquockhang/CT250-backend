package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.BookingMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingFlightRepository;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
import com.dvk.ct250backend.infrastructure.service.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    FlightRepository flightRepository;
    BookingMapper bookingMapper;
    BookingRepository bookingRepository;
    BookingFlightRepository bookingFlightRepository;
    RedisService redisService;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        if (booking.getBookingStatus() == BookingStatusEnum.PENDING || booking.getBookingStatus() == BookingStatusEnum.ON_HOLD) {
            redisService.set("Booking: " + UUID.randomUUID() , booking, 60 * 60 * 1000 * 3 );
        } else {
            booking = bookingRepository.save(booking);
            for (BookingFlight bookingFlight : booking.getBookingFlights()) {
                bookingFlight.setBooking(booking);
                Flight flight = flightRepository.findById(bookingFlight.getFlight().getFlightId()).orElseThrow();
                bookingFlight.setFlight(flight);
                bookingFlightRepository.save(bookingFlight);
            }
        }
        return bookingMapper.toBookingDTO(booking);
    }

    @Override
    public BookingDTO holdBooking(Long bookingId) {
//        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
//        redisService.set("Booking: " + booking.getPassengers(), booking );
//        return bookingMapper.toBookingDTO(booking);
        return null;
    }

}