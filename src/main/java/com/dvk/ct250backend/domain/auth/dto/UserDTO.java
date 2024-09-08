package com.dvk.ct250backend.domain.auth.dto;

import com.dvk.ct250backend.domain.common.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
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
    @NotBlank(message = "First name is required")
    String firstName;
    @NotBlank(message = "Last name is required")
    String lastName;
    @NotBlank(message = "Gender is required")
    String gender;
    @NotBlank(message = "Identity number is required")
    String identityNumber;
    @NotBlank(message = "Phone number is required")
    String phoneNumber;
    @NotNull(message = "Country ID is required")
    Integer countryId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull(message = "Date of birth is required")
    Date dateOfBirth;

    @NotNull(message = "Role ID is required")
    Integer roleId;
}