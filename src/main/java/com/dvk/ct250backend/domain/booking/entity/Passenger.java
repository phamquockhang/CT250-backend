package com.dvk.ct250backend.domain.booking.entity;

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
@Table(name = "passengers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passenger_id_seq")
    @SequenceGenerator(name = "passenger_id_seq", sequenceName = "passengers_seq", allocationSize = 1)
    Integer passengerId;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "passengers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Booking> bookings;
}
