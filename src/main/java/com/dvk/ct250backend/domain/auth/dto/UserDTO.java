package com.dvk.ct250backend.domain.auth.dto;

import com.dvk.ct250backend.domain.auth.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
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
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Password is required")
    String password;
    String firstName;
    String lastName;
    //GenderEnum gender;
    boolean gender;
    String id;
    String phoneNumber;
    Integer countryId;
    Set<String> roles = new HashSet<>();

}
