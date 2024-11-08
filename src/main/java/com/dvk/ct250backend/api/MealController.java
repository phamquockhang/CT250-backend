package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.MealDTO;
import com.dvk.ct250backend.domain.booking.service.MealService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MealController {

    MealService mealService;

    @PostMapping
    public ApiResponse<MealDTO> createMeal(@ModelAttribute MealDTO mealDTO, @RequestParam MultipartFile mealImg) throws IOException {
        return ApiResponse.<MealDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(mealService.createMeal(mealDTO, mealImg))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<MealDTO> updateMeal(@PathVariable("id") Integer mealId, @ModelAttribute MealDTO mealDTO, @RequestParam(required = false) MultipartFile mealImg) throws IOException, ResourceNotFoundException {
        return ApiResponse.<MealDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(mealService.updateMeal(mealId, mealDTO, mealImg))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMeal(@PathVariable("id") Integer mealId) throws ResourceNotFoundException {
        mealService.deleteMeal(mealId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @GetMapping
    public ApiResponse<Page<MealDTO>> getMeals(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<MealDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(mealService.getMeals(params))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<MealDTO>> getAllMeals() {
        return ApiResponse.<List<MealDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(mealService.getAllMeals())
                .build();
    }

}
