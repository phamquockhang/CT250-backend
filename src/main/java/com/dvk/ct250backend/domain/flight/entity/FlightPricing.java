package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.flight.enums.TicketClassNameEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight_pricing")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_pricing_id_seq")
    @SequenceGenerator(name = "flight_pricing_id_seq", sequenceName = "flight_pricing_seq", allocationSize = 1)
    Integer flightPricingId;

    Double ticketPrice;

    @Enumerated(EnumType.STRING)
    TicketClassNameEnum ticketClassName;
}
