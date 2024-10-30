package com.dvk.ct250backend.domain.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class MealPricingDTO {
    Integer mealPricingId;
    MealDTO meal;
    BigDecimal price;
    LocalDate validFrom;
    LocalDate validTo;
    Boolean isActive;
}
