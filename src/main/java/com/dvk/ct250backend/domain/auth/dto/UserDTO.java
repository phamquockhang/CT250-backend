package com.dvk.ct250backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    UUID userId;
    String email;
    String password;
    String firstName;
    String lastName;
    boolean gender;
    String id;
    String phoneNumber;
    int countryId;
    Set<String> roles;
}
