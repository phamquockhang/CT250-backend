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
        if (booking.getBookingStatus() == BookingStatusEnum.PENDING) {
            saveBookingToRedis(booking);
        } else {
            saveBookingToDB(booking);
        }
        return bookingMapper.toBookingDTO(booking);
    }

    private void saveBookingToRedis(Booking booking) {
        redisService.set("booking:" + booking.getBookingId(), booking );
    }

    private void saveBookingToDB(Booking booking) {
        booking = bookingRepository.save(booking);
        for (BookingFlight bookingFlight : booking.getBookingFlights()) {
            bookingFlight.setBooking(booking);
            Flight flight = flightRepository.findById(bookingFlight.getFlight().getFlightId()).orElseThrow();
            bookingFlight.setFlight(flight);
            bookingFlightRepository.save(bookingFlight);
        }
    }
}