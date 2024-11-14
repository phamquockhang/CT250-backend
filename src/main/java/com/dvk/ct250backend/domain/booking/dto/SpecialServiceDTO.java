package com.dvk.ct250backend.domain.booking.dto;

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
public class SpecialServiceDTO {
    Integer specialServiceId;
    String serviceName;
    String description;
    String conditions;
    Integer maxPassengers;
    Boolean requiredSupport;
    Boolean healthVerification;
    String bookingLeadTime;
    String specialInstructions;
    String serviceType;
    String imgUrl;
}
