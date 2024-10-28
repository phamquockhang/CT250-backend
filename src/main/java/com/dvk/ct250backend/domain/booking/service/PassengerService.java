package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.entity.Passenger;

import java.util.Map;

public interface PassengerService {
    Passenger handlePassenger(BookingPassenger bookingPassenger, Map<String, Passenger> passengerMap);
}