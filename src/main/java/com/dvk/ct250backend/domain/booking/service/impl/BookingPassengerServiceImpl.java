package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.dvk.ct250backend.domain.booking.repository.BookingPassengerRepository;
import com.dvk.ct250backend.domain.booking.service.BookingPassengerService;
import com.dvk.ct250backend.domain.booking.service.PassengerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingPassengerServiceImpl implements BookingPassengerService {

    BookingPassengerRepository bookingPassengerRepository;
    PassengerService passengerService;

    @Override
    public void processBookingPassengers(BookingFlight bookingFlight, Map<String, Passenger> passengerMap, AtomicBoolean isPrimaryContactSet) {
        bookingFlight.getBookingPassengers().forEach(bookingPassenger -> {
            Passenger passenger = passengerService.handlePassenger(bookingPassenger, passengerMap);
            bookingPassenger.setPassenger(passenger);

            setPrimaryContact(bookingPassenger, passenger, passengerMap, isPrimaryContactSet);
            bookingPassenger.setIsSharedSeat(passenger.getPassengerType() == PassengerTypeEnum.INFANT);
            bookingPassenger.setBookingFlight(bookingFlight);
            bookingPassenger.setBooking(bookingFlight.getBooking());
        });
    }

    private void setPrimaryContact(BookingPassenger bookingPassenger, Passenger passenger, Map<String, Passenger> passengerMap, AtomicBoolean isPrimaryContactSet) {
        if (passenger.getPassengerType() == PassengerTypeEnum.ADULT) {
            if (Boolean.TRUE.equals(bookingPassenger.getIsPrimaryContact()) || passengerMap.containsKey(passenger.getEmail())) {
                bookingPassenger.setIsPrimaryContact(true);
            } else {
                bookingPassenger.setIsPrimaryContact(!isPrimaryContactSet.getAndSet(true));
                passengerMap.put(passenger.getEmail(), passenger);
            }
        } else {
            bookingPassenger.setIsPrimaryContact(false);
        }
    }
}