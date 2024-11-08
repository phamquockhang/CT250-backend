package com.dvk.ct250backend.domain.transaction.dto;

import com.dvk.ct250backend.domain.transaction.dto.response.VNPayResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethodDTO {
    Integer paymentMethodId;
    @NotBlank(message = "Payment method name is required")
    String paymentMethodName;
    VNPayResponse vnPayResponse;
    String paymentUrl;
}