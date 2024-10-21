package com.dvk.ct250backend.domain.booking.entity;

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
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meal_id_seq")
    @SequenceGenerator(name = "meal_id_seq", sequenceName = "meals_seq", allocationSize = 1)
    Integer mealId;

    String name;
    String imageMeal;
    BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "meals", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<BookingFlight> bookingFlights;


}
