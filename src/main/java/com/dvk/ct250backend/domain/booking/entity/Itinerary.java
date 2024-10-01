package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.flight.entity.Flight;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itineraries")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itinerary_id_seq")
    @SequenceGenerator(name = "itinerary_id_seq", sequenceName = "itineraries_seq", allocationSize = 1)
    Integer itineraryId;
    String itineraryName;
    String itineraryDescription;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "booking_flight",
            joinColumns = @JoinColumn(name = "itinerary_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id"))
    Set<Flight> flights;
}
