package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.BookingMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.booking.service.BookingPassengerService;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import com.dvk.ct250backend.domain.booking.service.PassengerService;
import com.dvk.ct250backend.domain.booking.service.TicketService;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.entity.SeatAvailability;
import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
import com.dvk.ct250backend.domain.flight.service.FlightService;
import com.dvk.ct250backend.infrastructure.service.LockService;
import com.dvk.ct250backend.infrastructure.service.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    BookingMapper bookingMapper;
    BookingRepository bookingRepository;
    RedisService redisService;
    BookingFlightService bookingFlightService;
    BookingPassengerService bookingPassengerService;
    PassengerService passengerService;
    FlightService flightService;
    TicketService ticketService;
    LockService lockService;

    @Override
    @Transactional
    public BookingDTO createInitBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingStatus(BookingStatusEnum.INIT);
        return bookingMapper.toBookingDTO(bookingRepository.save(booking));
    }

}