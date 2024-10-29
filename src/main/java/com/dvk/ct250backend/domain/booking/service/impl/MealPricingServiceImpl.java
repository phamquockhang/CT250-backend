package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.MealPricingDTO;
import com.dvk.ct250backend.domain.booking.entity.MealPricing;
import com.dvk.ct250backend.domain.booking.mapper.MealPricingMapper;
import com.dvk.ct250backend.domain.booking.repository.MealPricingRepository;
import com.dvk.ct250backend.domain.booking.service.MealPricingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MealPricingServiceImpl implements MealPricingService {


    MealPricingRepository mealPricingRepository;
    MealPricingMapper mealPricingMapper;
    @Override
    public MealPricingDTO createMeal(MealPricingDTO mealPricingDTO) {
        MealPricing mealPricing = mealPricingMapper.toMealPricing(mealPricingDTO);
        return mealPricingMapper.toMealPricingDTO(mealPricingRepository.save(mealPricing));
    }

    @Override
    public MealPricingDTO updateMeal(Integer mealPricingId, MealPricingDTO mealPricingDTO) throws ResourceNotFoundException {
        MealPricing mealPricing = mealPricingRepository.findById(mealPricingId).orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
        mealPricingMapper.updateMealPricingFromDTO(mealPricing, mealPricingDTO);
        return mealPricingMapper.toMealPricingDTO(mealPricingRepository.save(mealPricing));
    }

    @Override
    public void deleteMeal(Integer mealPricingId) throws ResourceNotFoundException {
        MealPricing mealPricing = mealPricingRepository.findById(mealPricingId).orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
        mealPricingRepository.delete(mealPricing);
    }

    @Override
    public List<MealPricingDTO> getAllMeals() {
        return mealPricingRepository.findAll().stream().map(mealPricingMapper::toMealPricingDTO).collect(Collectors.toList());
    }
}
