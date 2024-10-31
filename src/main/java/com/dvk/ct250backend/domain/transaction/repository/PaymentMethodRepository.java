package com.dvk.ct250backend.domain.transaction.repository;

import com.dvk.ct250backend.domain.transaction.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
