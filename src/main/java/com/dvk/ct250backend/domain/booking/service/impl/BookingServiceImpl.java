package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.entity.*;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.dvk.ct250backend.domain.booking.mapper.BookingMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.booking.service.BookingPassengerService;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import com.dvk.ct250backend.domain.flight.entity.Seat;
import com.dvk.ct250backend.domain.flight.entity.SeatAvailability;
import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
import com.dvk.ct250backend.domain.flight.enums.TicketClassEnum;
import com.dvk.ct250backend.infrastructure.service.LockService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    BookingPassengerService bookingPassengerService;
    LockService lockService;
    BookingFlightService bookingFlightService;
    @Override
    @Transactional
    public BookingDTO createInitBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingStatus(BookingStatusEnum.INIT);
        Map<String, Passenger> passengerMap = new HashMap<>();
        AtomicBoolean isPrimaryContactSet = new AtomicBoolean(false);
        String passengerGroup = UUID.randomUUID().toString();

        booking.getBookingFlights().forEach(bookingFlight -> {
            bookingFlight.setBooking(booking);
            bookingPassengerService.processBookingPassengers(bookingFlight, passengerMap, isPrimaryContactSet, passengerGroup);
        });

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDTO(savedBooking);
    }

    @Override
    @Transactional
    public BookingDTO reserveBooking(Integer bookingId, BookingDTO bookingDTO) {
        String lockKey = "booking_" + bookingId;
        boolean lockAcquired = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
        if (!lockAcquired) {
            throw new RuntimeException("Could not acquire lock for booking " + bookingId);
        }
        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow();
            booking.setBookingStatus(BookingStatusEnum.RESERVED);
            booking.getBookingFlights().forEach(bookingFlightService::processBookingFlight);

            Booking savedBooking = bookingRepository.save(booking);
            return bookingMapper.toBookingDTO(savedBooking);
        } finally {
            lockService.releaseLock(lockKey);
        }
    }

}