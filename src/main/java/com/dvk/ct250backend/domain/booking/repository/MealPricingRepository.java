package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.MealPricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealPricingRepository extends JpaRepository<MealPricing, Integer> {
}
