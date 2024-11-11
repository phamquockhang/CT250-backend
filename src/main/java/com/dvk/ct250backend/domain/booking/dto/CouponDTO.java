package com.dvk.ct250backend.domain.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CouponDTO {
    Integer couponId;
    String couponCode;
    BigDecimal discountAmount;
    BigDecimal discountPercentage;
    LocalDate validFrom;
    LocalDate validTo;
    List<BookingDTO> bookings;
    LocalDate createdAt;
    LocalDate updatedAt;
}
