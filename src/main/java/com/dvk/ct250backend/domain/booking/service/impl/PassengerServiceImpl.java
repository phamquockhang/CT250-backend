package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.repository.PassengerRepository;
import com.dvk.ct250backend.domain.booking.service.PassengerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PassengerServiceImpl implements PassengerService {

    PassengerRepository passengerRepository;

    @Override
    public Passenger savePassenger(Passenger passenger) {
        if (passenger.getPassengerId() == null) {
            return passengerRepository.save(passenger);
        }
        return passenger;
    }
}
