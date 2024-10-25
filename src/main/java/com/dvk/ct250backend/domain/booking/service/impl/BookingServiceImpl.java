package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.BookingMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingFlightRepository;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.booking.service.BookingPassengerService;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import com.dvk.ct250backend.domain.booking.service.PassengerService;
import com.dvk.ct250backend.domain.booking.utils.BookingCodeUtils;
import com.dvk.ct250backend.domain.flight.service.FlightService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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
    FlightService flightService;
    BookingFlightRepository bookingFlightRepository;


    @Override
    @Transactional
    public BookingDTO createInitBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingStatus(BookingStatusEnum.INIT);
        Booking savedBooking = bookingRepository.save(booking);

        processBookingFlights(savedBooking, false);
        processBookingPassengers(savedBooking);

        return bookingMapper.toBookingDTO(savedBooking);
    }

    @Override
    @Transactional
    public BookingDTO reserveBooking(Integer bookingId, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        bookingMapper.updateBookingFromDTO(booking, bookingDTO);
        booking.setBookingCode(bookingCodeUtils.generateBookingCode());
        processBookingFlights(booking, true);
        return bookingMapper.toBookingDTO(bookingRepository.save(booking));
    }


    private void processBookingFlights(Booking savedBooking, boolean reserveSeat) {
        savedBooking.getBookingFlights().forEach(bookingFlight -> {
            bookingFlight.setBooking(savedBooking);
            bookingFlightService.saveBookingFlight(bookingFlight, reserveSeat);
        });
    }
    private void processBookingPassengers(Booking savedBooking) {
        savedBooking.getBookingPassengers().forEach(bookingPassenger -> {
            Passenger passenger = passengerService.savePassenger(bookingPassenger.getPassenger());
            if (passenger.getPassengerId() == null) {
                passenger = passengerService.savePassenger(passenger);
            }
            bookingPassenger.setPassenger(passenger);
            bookingPassenger.setBooking(savedBooking);
            bookingPassengerService.saveBookingPassenger(bookingPassenger);
        });
    }

}

