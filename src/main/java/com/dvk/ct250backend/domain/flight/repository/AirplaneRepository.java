package com.dvk.ct250backend.domain.flight.repository;

import com.dvk.ct250backend.domain.flight.entity.Airplane;
import com.dvk.ct250backend.domain.flight.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AirplaneRepository extends JpaRepository<Airplane, Integer>, JpaSpecificationExecutor<Airplane> {
    boolean existsByModel(Model model);
}
