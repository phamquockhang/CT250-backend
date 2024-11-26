package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> , JpaSpecificationExecutor<Booking> {
    List<Booking> findByBookingStatusAndCreatedAtBefore(BookingStatusEnum status, LocalDateTime cutoffTime);
    Optional<Booking> findByBookingCode(String bookingCode);
    List<Booking> findAllByBookingStatus(BookingStatusEnum bookingStatus);
    @Query("SELECT b FROM Booking b WHERE b.createdAt >= :startDate AND b.createdAt < :endDate")
    List<Booking> findBookingInRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
