package com.dvk.ct250backend.domain.user;

import com.dvk.ct250backend.domain.CountryDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    String email;
    String password;
    String firstName;
    String lastName;
    boolean gender;
    String id;
    String phoneNumber;
    CountryDTO country;
    Set<RoleDTO> roles;
}
