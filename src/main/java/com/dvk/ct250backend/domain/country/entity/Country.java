package com.dvk.ct250backend.domain.country.entity;

import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_id_seq")
    @SequenceGenerator(name = "country_id_seq", sequenceName = "countries_seq", allocationSize = 1)

    Integer countryId = 0;
    @Column(name = "iso2_code")
    String iso2Code;
    @Column(name = "iso3_code")
    String iso3Code;
    String countryName;
    Integer countryCode;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<User> users;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    Set<Airport> airports;

//    public Country(Integer countryId, String countryName, Integer countryCode , String iso2Code, String iso3Code) {
//        this.countryId = countryId;
//        this.iso2Code = iso2Code;
//        this.iso3Code = iso3Code;
//        this.countryName = countryName;
//        this.countryCode = countryCode;
//    }
}
