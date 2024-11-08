package com.dvk.ct250backend.domain.booking.dto;

import com.dvk.ct250backend.domain.auth.enums.GenderEnum;
import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassengerDTO {
    String passengerId;
    @NotBlank(message = "Passenger name is required")
    String passengerType;

    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;
    LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    GenderEnum gender;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @NotBlank
    Boolean isPrimaryContact;

    CountryDTO country;
}
