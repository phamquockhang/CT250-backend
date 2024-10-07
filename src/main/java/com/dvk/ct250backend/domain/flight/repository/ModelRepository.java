package com.dvk.ct250backend.domain.flight.repository;

import com.dvk.ct250backend.domain.flight.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository
        extends JpaRepository<Model, Integer> {
}
