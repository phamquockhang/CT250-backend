package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.MealDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MealService {
    MealDTO createMeal(MealDTO mealDTO, MultipartFile imgUrl) throws IOException;
    MealDTO updateMeal(Integer mealId, MealDTO mealDTO, MultipartFile imgUrl) throws IOException, ResourceNotFoundException;
    void deleteMeal(Integer mealId) throws ResourceNotFoundException;
    List<MealDTO> getAllMeals();
    Page<MealDTO> getMeals(Map<String, String> params);
}
