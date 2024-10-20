package com.dvk.ct250backend.domain.flight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeeDTO {
    Integer feeId;

    @NotBlank(message = "Fee name is required")
    String feeName;

    List<FeePricingDTO> feePricing;

    FeeGroupDTO feeGroup;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
