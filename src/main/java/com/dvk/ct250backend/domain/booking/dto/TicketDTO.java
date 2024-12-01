package com.dvk.ct250backend.domain.booking.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDTO {
    Integer ticketId;
    String ticketNumber;
    String pdfUrl;
    String status;
    BookingPassengerDTO bookingPassenger;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    //response
    String passengerGroup;
    String passengerName;
    String bookingCode;
    String phoneNumber;
}
