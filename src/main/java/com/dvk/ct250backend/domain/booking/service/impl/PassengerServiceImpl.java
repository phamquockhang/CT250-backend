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
        String uniqueIdentifier = getUniqueIdentifier(passenger, bookingPassenger.getPassengerGroup());

        Passenger existingPassenger = passengerMap.get(uniqueIdentifier);
        if (existingPassenger == null) {
            existingPassenger = passengerRepository.findByEmailAndIsPrimaryContact(passenger.getEmail())
                    .orElseGet(() -> passengerRepository.save(passenger));
            passengerMap.put(uniqueIdentifier, existingPassenger);
        }
        return existingPassenger;
    }

    private String getUniqueIdentifier(Passenger passenger, String passengerGroup) {
        if (passenger.getEmail() != null && !passenger.getEmail().isEmpty()) {
            return passenger.getEmail();
        }
        return passenger.getPassengerType().toString() + "-" +passenger.getFirstName() + "_" + passenger.getLastName() + "_" + passenger.getDateOfBirth() + passengerGroup;
    }
}

