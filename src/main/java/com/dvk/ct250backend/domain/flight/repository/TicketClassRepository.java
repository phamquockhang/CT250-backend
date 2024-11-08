package com.dvk.ct250backend.domain.flight.repository;

import com.dvk.ct250backend.domain.flight.entity.TicketClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketClassRepository extends JpaRepository<TicketClass, Integer> {
}
