package com.dvk.ct250backend.domain.auth.dto;

import com.dvk.ct250backend.domain.country.dto.CountryDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    String email;
    String password;
    String firstName;
    String lastName;
    boolean gender;
    String id;
    String phoneNumber;
    CountryDTO country;
    //Set<RoleDTO> roles;
}
