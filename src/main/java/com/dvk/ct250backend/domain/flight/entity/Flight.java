package com.dvk.ct250backend.domain.flight.entity;

//import com.dvk.ct250backend.domain.booking.entity.Booking;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.enums.FlightStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight extends BaseEntity {

    @Id
    String flightId;

    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id")
    Route route;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flight", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<FlightPricing> flightPricing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id", nullable = false)
    Airplane airplane;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flight", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<SeatAvailability> seatAvailability;

    @Enumerated(EnumType.STRING)
    FlightStatusEnum flightStatus;

//    @OneToMany(mappedBy = "destinationFlight", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
//    Set<Booking> destinationBookings;
//
//
//    @OneToMany(mappedBy = "departureFlight", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
//    Set<Booking> departureBookings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "flight_fee",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "fee_id")
    )
    List<Fee> fees;


}
