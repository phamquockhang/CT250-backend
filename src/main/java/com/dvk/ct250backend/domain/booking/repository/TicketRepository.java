package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketRepository extends JpaRepository<Ticket, Integer> , JpaSpecificationExecutor<Ticket> {
}
