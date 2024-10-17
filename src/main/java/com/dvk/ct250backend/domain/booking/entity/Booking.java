package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.booking.enums.TripTypeEnum;
import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_seq")
    @SequenceGenerator(name = "booking_id_seq", sequenceName = "bookings_seq", allocationSize = 1)
    Integer bookingId;

    @Enumerated(EnumType.STRING)
    TripTypeEnum tripType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_flight_id")
    Flight departureFlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_flight_id")
    Flight destinationFlight;

    @OneToMany(mappedBy = "booking", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Set<BookingSegment> flightSegments;

    @OneToMany(mappedBy = "booking", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Set<Ticket> tickets;

    Double totalPrice;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinTable(name = "booking_passenger", joinColumns = @JoinColumn(name = "booking_id"), inverseJoinColumns = @JoinColumn(name = "passenger_id"))
List<Passenger> passengers;

}
