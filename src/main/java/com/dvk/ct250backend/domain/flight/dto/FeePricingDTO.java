package com.dvk.ct250backend.domain.flight.dto;

import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeePricingDTO {
    Integer feePricingId;
    PassengerTypeEnum passengerType;
}
