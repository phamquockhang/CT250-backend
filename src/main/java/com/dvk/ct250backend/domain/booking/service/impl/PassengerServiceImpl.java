package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.repository.PassengerRepository;
import com.dvk.ct250backend.domain.booking.service.PassengerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PassengerServiceImpl implements PassengerService {

    PassengerRepository passengerRepository;

    @Override
    public Passenger handlePassenger(BookingPassenger bookingPassenger, Map<String, Passenger> passengerMap) {
        Passenger passenger = bookingPassenger.getPassenger();
        return passengerMap.computeIfAbsent(passenger.getEmail(), email -> {
            return passengerRepository.findByEmailAndIsPrimaryContact(email)
                    .orElseGet(() -> passengerRepository.save(passenger));
        });
    }
}