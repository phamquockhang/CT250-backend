package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.dvk.ct250backend.domain.flight.enums.RouteTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fee_pricing")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeePricing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_pricing_id_seq")
    @SequenceGenerator(name = "flight_pricing_id_seq", sequenceName = "flight_pricing_seq", allocationSize = 1)
    Integer feePricingId;

    @ManyToOne
    @JoinColumn(name = "fee_id")
    Fee fee;

    @Enumerated(EnumType.STRING)
    PassengerTypeEnum passengerType;

    @Column(precision = 15, scale = 2)
    BigDecimal feeAmount;
    Boolean isPercentage;

    LocalDate validFrom;
    LocalDate validTo;

    @Enumerated(EnumType.STRING)
    RouteTypeEnum routeType;

}
