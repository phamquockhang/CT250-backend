package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.entity.*;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.BookingMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingPassengerService;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    BookingPassengerService bookingPassengerService;

    @Override
    @Transactional
    public BookingDTO createInitBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingStatus(BookingStatusEnum.INIT);

        Map<String, Passenger> passengerMap = new HashMap<>();
        AtomicBoolean isPrimaryContactSet = new AtomicBoolean(false);

        booking.getBookingFlights().forEach(bookingFlight -> {
            bookingFlight.setBooking(booking);
            bookingPassengerService.processBookingPassengers(bookingFlight, passengerMap, isPrimaryContactSet);
        });

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDTO(savedBooking);
    }
}