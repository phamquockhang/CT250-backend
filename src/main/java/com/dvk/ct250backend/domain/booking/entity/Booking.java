package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.enums.TripTypeEnum;
import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "booking", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<BookingFlight> bookingFlights;

    @OneToMany(mappedBy = "booking", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Set<Ticket> tickets;

    @Column(precision = 15, scale = 2)
    BigDecimal totalPrice;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "booking_passenger", joinColumns = @JoinColumn(name = "booking_id"), inverseJoinColumns = @JoinColumn(name = "passenger_id"))
    List<Passenger> passengers;

    @Enumerated(EnumType.STRING)
    BookingStatusEnum bookingStatus;
}
