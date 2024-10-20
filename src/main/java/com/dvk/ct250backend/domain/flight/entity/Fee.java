package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
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
@Table(name = "fees")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_id_seq")
    @SequenceGenerator(name = "fee_id_seq", sequenceName = "fees_seq", allocationSize = 1)
    Integer feeId;
    String feeName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fee", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<FeePricing> feePricing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_group_id")
    FeeGroup feeGroup;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "fees", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Flight> flights;

}
