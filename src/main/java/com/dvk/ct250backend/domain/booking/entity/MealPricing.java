package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meal_pricing")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealPricing extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meal_pricing_id_seq")
    @SequenceGenerator(name = "meal_pricing_id_seq", sequenceName = "meal_pricing_seq", allocationSize = 1)
    Integer mealPricingId;

    BigDecimal price;
    LocalDate validFrom;
    LocalDate validTo;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    Meal meal;


}
