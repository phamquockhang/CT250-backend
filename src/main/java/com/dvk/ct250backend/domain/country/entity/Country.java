package com.dvk.ct250backend.domain.country.entity;

import com.dvk.ct250backend.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_id_seq")
//    @SequenceGenerator(name = "country_id_seq", sequenceName = "countries_seq", allocationSize = 1)
    Integer countryId;
    String iso2Code;
    String iso3Code;
    String countryName;
    Integer countryCode;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<User> users;
}
