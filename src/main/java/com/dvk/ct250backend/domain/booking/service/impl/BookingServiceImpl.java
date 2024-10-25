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
    BookingMapper bookingMapper;
    BookingRepository bookingRepository;
    RedisService redisService;
    BookingFlightService bookingFlightService;
    BookingPassengerService bookingPassengerService;
    PassengerService passengerService;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        String redisKey = "Booking: " + booking.getBookingId();
        if (booking.getBookingStatus() == BookingStatusEnum.PENDING) {
            redisService.set(redisKey, booking, 60 * 60 * 1000 * 3); // 3 hour
        } else {
            redisService.delete(redisKey);
            booking = bookingRepository.save(booking);
            for (BookingFlight bookingFlight : booking.getBookingFlights()) {
                bookingFlight.setBooking(booking);
                bookingFlightService.saveBookingFlight(bookingFlight);
            }
            for (BookingPassenger bookingPassenger : booking.getBookingPassengers()) {
                Passenger passenger = passengerService.savePassenger(bookingPassenger.getPassenger());
                bookingPassenger.setPassenger(passenger);
                bookingPassenger.setBooking(booking);
                bookingPassengerService.saveBookingPassenger(bookingPassenger);
            }
        }
        return bookingMapper.toBookingDTO(booking);
    }
}