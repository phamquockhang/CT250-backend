package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.dvk.ct250backend.domain.country.entity.Country;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID passengerId;

    @Column(unique = true)
    String email;

    String firstName;
    String lastName;
    LocalDate dateOfBirth;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    PassengerTypeEnum passengerType;

    @ManyToOne
    @JoinColumn(name = "country_id")
    Country country;

    @OneToMany(mappedBy = "passenger", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<BookingPassenger> bookingPassengers;

}
