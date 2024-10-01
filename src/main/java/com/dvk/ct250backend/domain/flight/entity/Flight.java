package com.dvk.ct250backend.domain.flight.entity;


import com.dvk.ct250backend.domain.booking.entity.Itinerary;
import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Set;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_id_seq")
    @SequenceGenerator(name = "flight_id_seq", sequenceName = "flights_seq", allocationSize = 1)
    Integer flightId;

    @Column(unique = true)
    String flightName;

    Timestamp departureDateTime;
    Timestamp arrivalDateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id")
    Route route;



}
