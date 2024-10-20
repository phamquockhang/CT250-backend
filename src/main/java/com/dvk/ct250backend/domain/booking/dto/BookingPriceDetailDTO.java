package com.dvk.ct250backend.domain.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingPriceDetailDTO {

  Integer passengerPriceDetailId;

  String passengerType;

  Double priceAmount;
}
