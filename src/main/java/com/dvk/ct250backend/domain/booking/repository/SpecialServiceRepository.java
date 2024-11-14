package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.SpecialService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpecialServiceRepository extends JpaRepository<SpecialService, Integer>, JpaSpecificationExecutor<SpecialService> {
}
