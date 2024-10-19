package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.BookingPriceDetailLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingPriceDetailLineRepository extends JpaRepository<BookingPriceDetailLine, Integer>, JpaSpecificationExecutor<BookingPriceDetailLine> {
}
