package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seat_availability")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_availability_id_seq")
    @SequenceGenerator(name = "seat_availability_id_seq", sequenceName = "seat_availability_seq", allocationSize = 1)
    Integer seatAvailabilityId;

    Integer totalSeats;
    Integer bookedSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    Seat seat;

    @Enumerated(EnumType.STRING)
   SeatAvailabilityStatus status;

   String position;

}
