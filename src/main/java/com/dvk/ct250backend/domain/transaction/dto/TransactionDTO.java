package com.dvk.ct250backend.domain.transaction.dto;

import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    Integer transactionId;

    @NotBlank(message = "Booking is required")
    BookingDTO booking;

    @NotBlank(message = "Payment method is required")
    PaymentMethodDTO paymentMethod;

    String transactionType;

    @NotBlank(message = "Status is required")
    String status;

    BigDecimal amount;
}
