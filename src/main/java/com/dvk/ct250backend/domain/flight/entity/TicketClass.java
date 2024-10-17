package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.enums.TicketClassEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket_class")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketClass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_class_id_seq")
    @SequenceGenerator(name = "ticket_class_id_seq", sequenceName = "ticket_class_seq", allocationSize = 1)
    Integer ticketClassId;


    @Enumerated(EnumType.STRING)
    TicketClassEnum ticketClassName;

    String luggageAllowance;
    String checkedBaggageAllowance;

    Double refundFeeBefore;
    Double refundFeeAfter;
    Double changeFeeBefore;
    Double changeFeeAfter;
    Boolean isSeatSelectionFree;

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticketClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
   List<FlightPricing> flightPricing;

}
