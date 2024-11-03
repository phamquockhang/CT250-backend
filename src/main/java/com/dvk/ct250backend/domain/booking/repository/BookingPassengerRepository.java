package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingPassengerRepository extends JpaRepository<BookingPassenger, Integer>, JpaSpecificationExecutor<BookingPassenger> {
}
