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
    @GeneratedValue
    Integer countryId;
    String countryName;
    Integer countryCode;

    @OneToMany(mappedBy = "country")
    Set<User> users;
}
