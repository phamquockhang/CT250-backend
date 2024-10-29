package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.enums.RouteTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "baggages")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Baggage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "baggage_id_seq")
    @SequenceGenerator(name = "baggage_id_seq", sequenceName = "baggage_seq", allocationSize = 1)
    Integer baggageId;

    Integer baggageWeight;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "baggage",  cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<BookingPassenger> bookingPassengers;

    @Enumerated(EnumType.STRING)
    RouteTypeEnum routeType;

    @OneToMany(mappedBy = "baggage", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<BaggagePricing> baggagePricing;

}
