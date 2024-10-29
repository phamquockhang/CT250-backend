package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.Meal;
import com.dvk.ct250backend.domain.booking.entity.MealPricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealPricingRepository extends JpaRepository<MealPricing, Integer> {
}
