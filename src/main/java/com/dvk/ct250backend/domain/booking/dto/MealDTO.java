package com.dvk.ct250backend.domain.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MealDTO {
    Integer mealId;

    @NotBlank(message = "Meal name is required")
    String mealName;

    String imgUrl;

    @NotBlank(message = "Meal pricing is required")
    List<MealPricingDTO> mealPricing;

    LocalDate createdAt;
    LocalDate updatedAt;
}
