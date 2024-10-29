package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meals")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meal_id_seq")
    @SequenceGenerator(name = "meal_id_seq", sequenceName = "meals_seq", allocationSize = 1)
    Integer mealId;

    String mealName;
    String imgUrl;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "meals", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<BookingPassenger> bookingPassengers;

    @OneToMany(mappedBy = "meal", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<MealPricing> mealPricing;

}
