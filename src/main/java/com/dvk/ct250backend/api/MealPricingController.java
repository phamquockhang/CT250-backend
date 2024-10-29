package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.MealPricingDTO;
import com.dvk.ct250backend.domain.booking.service.MealPricingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meal-pricing")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MealPricingController {
    MealPricingService mealPricingService;

    @GetMapping("/all")
    public ApiResponse<List<MealPricingDTO>> getAllMeals() {
        return ApiResponse.<List<MealPricingDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(mealPricingService.getAllMeals())
                .build();
    }

    @PostMapping
    public ApiResponse<MealPricingDTO> createMeal(MealPricingDTO mealPricingDTO) {
        return ApiResponse.<MealPricingDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(mealPricingService.createMeal(mealPricingDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<MealPricingDTO> updateMeal(@PathVariable("id") Integer mealPricingId, @RequestBody MealPricingDTO mealPricingDTO) throws ResourceNotFoundException {
        return ApiResponse.<MealPricingDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(mealPricingService.updateMeal(mealPricingId, mealPricingDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMeal(@PathVariable("id") Integer mealPricingId) throws ResourceNotFoundException {
        mealPricingService.deleteMeal(mealPricingId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }
}
