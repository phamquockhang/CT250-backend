package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.flight.entity.Seat;
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
@Table(name = "booking_passenger")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingPassenger {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_passenger_id_seq")
    @SequenceGenerator(name = "booking_passenger_id_seq", sequenceName = "booking_passengers_seq", allocationSize = 1)
    Integer bookingPassengerId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="passenger_id")
    Passenger passenger;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "booking_passenger_meal", joinColumns = @JoinColumn(name = "booking_passenger_id"), inverseJoinColumns = @JoinColumn(name = "meal_id"))
    List<Meal> meals;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "booking_passenger_special_service", joinColumns = @JoinColumn(name = "booking_passenger_id"), inverseJoinColumns = @JoinColumn(name = "special_service_id"))
    List<SpecialService> specialServices;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="baggage_id")
    Baggage baggage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_flight_id")
    BookingFlight bookingFlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    Seat seat;

    @Version
    Integer version;

    Boolean isPrimaryContact;
    Boolean isSharedSeat;
    String passengerGroup;

    @OneToMany(mappedBy = "bookingPassenger", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<Ticket> tickets;
}
