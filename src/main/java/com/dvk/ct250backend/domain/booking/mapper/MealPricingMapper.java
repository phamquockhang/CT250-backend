package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.MealPricingDTO;
import com.dvk.ct250backend.domain.booking.entity.MealPricing;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface MealPricingMapper {
    MealPricingDTO toMealPricingDTO(MealPricing mealPricing);
    MealPricing toMealPricing(MealPricingDTO mealPricingDTO);
    void updateMealPricingFromDTO(@MappingTarget MealPricing mealPricing, MealPricingDTO mealPricingDTO);
}
