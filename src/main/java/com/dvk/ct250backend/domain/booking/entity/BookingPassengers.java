package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.dvk.ct250backend.domain.flight.entity.FlightPricing;
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
@Table(name = "booking_passengers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingPassengers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_passenger_id_seq")
    @SequenceGenerator(name = "booking_passenger_id_seq", sequenceName = "booking_passengers_seq", allocationSize = 1)
    Integer bookingPassengerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="booking_id")
    Booking booking;

    @Enumerated(EnumType.STRING)
    PassengerTypeEnum passengerType;

}
