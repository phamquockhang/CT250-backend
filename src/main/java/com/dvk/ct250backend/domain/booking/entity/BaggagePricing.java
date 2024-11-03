package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
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
@Table(name = "baggage_pricing")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaggagePricing extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "baggage_pricing_id_seq")
    @SequenceGenerator(name = "baggage_pricing_id_seq", sequenceName = "baggage_pricing_seq", allocationSize = 1)
    Integer baggagePricingId;

    BigDecimal price;
    LocalDate validFrom;
    LocalDate validTo;

    @ManyToOne
    @JoinColumn(name = "baggage_id")
    Baggage baggage;
}
