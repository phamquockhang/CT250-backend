package com.dvk.ct250backend.domain;

import com.dvk.ct250backend.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    Integer countryId;
    String countryName;
    Integer countryCode;

    @OneToMany(mappedBy = "country")
    Set<User> users;
}
