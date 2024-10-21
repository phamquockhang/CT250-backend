package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.MealDTO;
import com.dvk.ct250backend.domain.booking.entity.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MealMapper {
    MealDTO toMealDTO(Meal meal);
    Meal toMeal(MealDTO mealDTO);
    void updateMealFromDTO(@MappingTarget Meal meal, MealDTO mealDTO);
}
