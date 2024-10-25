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
import com.dvk.ct250backend.domain.booking.utils.BookingCodeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    BookingMapper bookingMapper;
    BookingRepository bookingRepository;
    BookingCodeUtils bookingCodeUtils;
    BookingFlightService bookingFlightService;
    BookingPassengerService bookingPassengerService;
    PassengerService passengerService;

    @Override
    @Transactional
    public BookingDTO createInitBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingStatus(BookingStatusEnum.INIT);
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
        return bookingMapper.toBookingDTO(booking);
    }

    @Override
    @Transactional
    public BookingDTO reserveBooking(Integer bookingId, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        bookingMapper.updateBookingFromDTO(booking, bookingDTO);
        booking.setBookingCode(bookingCodeUtils.generateBookingCode());
        return bookingMapper.toBookingDTO(bookingRepository.save(booking));
    }

}