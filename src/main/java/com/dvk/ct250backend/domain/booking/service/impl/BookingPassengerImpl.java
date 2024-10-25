package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.entity.Meal;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.repository.BookingPassengerRepository;
import com.dvk.ct250backend.domain.booking.repository.MealRepository;
import com.dvk.ct250backend.domain.booking.service.BookingPassengerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingPassengerImpl implements BookingPassengerService {

    MealRepository mealRepository;
    BookingPassengerRepository bookingPassengerRepository;

    @Override
    public void saveBookingPassenger(BookingPassenger bookingPassenger) {
        for (Meal meal : bookingPassenger.getMeals()) {
            if (meal.getMealId() == null) {
                meal = mealRepository.save(meal);
            } else {
                meal = mealRepository.findById(meal.getMealId()).orElseThrow();
            }
        }
        bookingPassengerRepository.save(bookingPassenger);
    }
}
