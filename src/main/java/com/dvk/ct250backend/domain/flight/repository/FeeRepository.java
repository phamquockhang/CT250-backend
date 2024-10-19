package com.dvk.ct250backend.domain.flight.repository;

import com.dvk.ct250backend.domain.flight.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Integer> {
}
