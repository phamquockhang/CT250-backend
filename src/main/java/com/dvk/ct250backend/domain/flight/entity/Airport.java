package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.country.entity.Country;
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
@Table(name = "airports")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Airport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airport_id_seq")
    @SequenceGenerator(name = "airport_id_seq", sequenceName = "airports_seq", allocationSize = 1)
    Integer airportId;
    String airportName;

    String airportCode;
    String cityName;
    String cityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    Country country;

    @OneToMany(mappedBy = "destinationAirport")
    Set<Route> destinationRoutes;

    @OneToMany(mappedBy = "arrivalAirport")
    Set<Route> arrivalRoutes;

}
