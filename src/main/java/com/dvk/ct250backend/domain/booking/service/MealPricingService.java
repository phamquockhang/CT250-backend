package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.MealPricingDTO;

import java.util.List;

public interface MealPricingService {
    MealPricingDTO createMeal(MealPricingDTO mealPricingDTO);
    MealPricingDTO updateMeal(Integer mealPricingId, MealPricingDTO mealPricingDTO) throws ResourceNotFoundException;
    void deleteMeal(Integer mealPricingId) throws ResourceNotFoundException;
    List<MealPricingDTO> getAllMeals();
}
