package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.country.entity.Country;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airports")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airport_id_seq")
    @SequenceGenerator(name = "airport_id_seq", sequenceName = "airports_seq", allocationSize = 1)
    @Column(name = "airport_id", columnDefinition = "BIGINT default nextval('airports_seq')")
    Integer airportId;
    String airportName;
    String airportCode;
    String cityName;
    String cityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    Country country;

}
