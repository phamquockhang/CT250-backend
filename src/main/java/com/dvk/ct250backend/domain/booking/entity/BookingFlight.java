package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.flight.entity.Flight;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_flight")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingFlight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_flight_id_seq")
    @SequenceGenerator(name = "booking_flight_id_seq", sequenceName = "booking_flights_seq", allocationSize = 1)
    Integer bookingFlightId;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    Flight flight;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    Booking booking;


}
