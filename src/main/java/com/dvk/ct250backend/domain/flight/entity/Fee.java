package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String feeType;
    Double feeAmount;
    Boolean isPercentage;

    @Enumerated(EnumType.STRING)
    PassengerTypeEnum passengerType;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "fees", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Flight> flights;


}
