package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.PassengerDTO;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.entity.Passenger;

import java.util.Map;

public interface PassengerService {
    Passenger handlePassenger(BookingPassenger bookingPassenger, Map<String, Passenger> passengerMap);
    Page<PassengerDTO> getPassengers(Map<String, String> params);
    void deletePassenger(Integer id) throws ResourceNotFoundException;
    PassengerDTO updatePassenger(Integer id, PassengerDTO passengerDTO) throws ResourceNotFoundException;
    Map<String, Integer> getPassengerShareStats();
}