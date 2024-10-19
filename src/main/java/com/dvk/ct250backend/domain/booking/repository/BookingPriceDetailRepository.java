package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.BookingPriceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingPriceDetailRepository extends JpaRepository<BookingPriceDetail, Integer>, JpaSpecificationExecutor<BookingPriceDetail> {
}
