package com.dvk.ct250backend.domain.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponDTO {
    Integer couponId;
    String couponCode;
    BigDecimal discountValue;

    @NotBlank(message = "Coupon type is required")
    String couponType;
    @NotBlank(message = "Max usage is required")
    Integer maxUsage;
    LocalDate validFrom;
    LocalDate validTo;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
