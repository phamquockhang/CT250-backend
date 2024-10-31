package com.dvk.ct250backend.domain.booking.repository;

import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long>, JpaSpecificationExecutor<Passenger> {

    @Query("SELECT p FROM Passenger p JOIN BookingPassenger bp ON p.passengerId = bp.passenger.passengerId WHERE p.email = :email AND bp.isPrimaryContact = true")
    Optional<Passenger> findByEmailAndIsPrimaryContact(@Param("email") String email);

    @Query("SELECT p FROM Passenger p JOIN BookingPassenger bp ON p.passengerId = bp.passenger.passengerId " +
            "JOIN BookingFlight bf ON bp.bookingFlight.bookingFlightId = bf.bookingFlightId " +
            "JOIN Booking b ON bf.booking.bookingId = b.bookingId " +
            "WHERE p.email = :email AND bp.isPrimaryContact = true AND b.bookingStatus != :status")
    Optional<Passenger> findByEmailAndIsPrimaryContactAndBookingStatusNot(@Param("email") String email, @Param("status") BookingStatusEnum status);

}
