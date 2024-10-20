package com.dvk.ct250backend.domain.flight.dto;

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
public class TicketClassDTO {

    Integer ticketClassId;

    @NotBlank(message = "Ticket class name is required")
    String ticketClassName;

    @NotBlank(message = "Luggage allowance is required")
    String luggageAllowance;

    @NotBlank(message = "Checked baggage allowance is required")
    String checkedBaggageAllowance;

    @NotBlank(message = "Refund fee before is required")
    BigDecimal refundFeeBefore;

    @NotBlank(message = "Refund fee after is required")
    BigDecimal refundFeeAfter;

    @NotBlank(message = "Change fee before is required")
    BigDecimal changeFeeBefore;

    @NotBlank(message = "Change fee after is required")
    BigDecimal changeFeeAfter;

    @NotBlank(message = "Is seat selection free is required")
    Boolean isSeatSelectionFree;


}
