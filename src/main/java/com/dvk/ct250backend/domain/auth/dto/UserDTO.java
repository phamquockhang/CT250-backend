package com.dvk.ct250backend.domain.auth.dto;

import com.dvk.ct250backend.domain.common.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseDTO {
    UUID userId;
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Password is required")
    String password;
    String firstName;
    String lastName;
    String gender;
    String identificationNumber;
    String passportNumber;
    String phoneNumber;
    Integer countryId;
}