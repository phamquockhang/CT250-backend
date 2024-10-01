package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    String freeName;
    Integer amount;
}
