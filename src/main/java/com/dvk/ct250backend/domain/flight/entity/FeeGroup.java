package com.dvk.ct250backend.domain.flight.entity;

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
@Table(name = "fee_groups")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeeGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_group_id_seq")
    @SequenceGenerator(name = "fee_group_id_seq", sequenceName = "fee_group_seq", allocationSize = 1)
    Integer feeGroupId;

    String feeGroupName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feeGroup")
    List<Fee> fees;
}
