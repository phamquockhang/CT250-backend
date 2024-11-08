package com.dvk.ct250backend.domain.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaggageDTO {
    Integer baggageId;

    @NotBlank(message = "Kilogram is required")
    Integer baggageWeight;

    @NotBlank(message = "Price is required")
    BigDecimal price;

    @NotBlank(message = "Route type is required")
    String routeType;

    LocalDate createdAt;
    LocalDate updatedAt;

    @NotBlank(message = "Baggage pricing is required")
    List<BaggagePricingDTO> baggagePricing;

}
