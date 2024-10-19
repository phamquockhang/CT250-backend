package com.dvk.ct250backend.domain.flight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeeDTO {
    Integer feeId;

    @NotBlank(message = "Fee type is required")
    String feeType;

    @NotBlank(message = "Fee amount is required")
    Double feeAmount;

    @NotBlank(message = "Is percentage is required")
    Boolean isPercentage;

    @NotBlank(message = "Passenger type is required")
    String passengerType;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
