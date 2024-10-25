package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.booking.enums.TicketStatusEnum;
import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_seq")
    @SequenceGenerator(name = "ticket_id_seq", sequenceName = "tickets_seq", allocationSize = 1)
    Integer ticketId;

    @Enumerated(EnumType.STRING)
    TicketStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    Booking booking;

    String seatCode;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    Flight flight;

}
