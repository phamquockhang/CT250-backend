package com.dvk.ct250backend.domain.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @NotBlank(message = "Price is required")
    BigDecimal price;

    LocalDate createdAt;
    LocalDate updatedAt;
}
