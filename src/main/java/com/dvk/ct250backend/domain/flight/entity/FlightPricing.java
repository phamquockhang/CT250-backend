package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.enums.TicketClassEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight_pricing")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightPricing extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_pricing_id_seq")
    @SequenceGenerator(name = "flight_pricing_id_seq", sequenceName = "flight_pricing_seq", allocationSize = 1)
    Integer flightPricingId;

    Double ticketPrice;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "seat_id")
//    Seat seat;

    @Enumerated(EnumType.STRING)
    TicketClassEnum ticketClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    Flight flight;

    @Column(name = "valid_from")
    LocalDate validFrom;

    @Column(name = "valid_to")
    LocalDate validTo;
}
