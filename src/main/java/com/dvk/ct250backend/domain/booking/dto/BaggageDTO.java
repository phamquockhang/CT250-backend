package com.dvk.ct250backend.domain.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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

}
