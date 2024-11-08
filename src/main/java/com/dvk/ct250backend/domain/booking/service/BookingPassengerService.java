package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import com.dvk.ct250backend.domain.booking.entity.Passenger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface BookingPassengerService {
    void processBookingPassengers(BookingFlight bookingFlight, Map<String, Passenger> passengerMap, AtomicBoolean isPrimaryContactSet, String passengerGroup);

}