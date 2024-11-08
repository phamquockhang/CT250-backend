package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MealRepository extends JpaRepository<Meal, Integer>, JpaSpecificationExecutor<Meal> {
}
