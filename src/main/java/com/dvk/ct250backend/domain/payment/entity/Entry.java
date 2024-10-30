package com.dvk.ct250backend.domain.payment.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entries")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Entry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entry_id_seq")
    @SequenceGenerator(name = "entry_id_seq", sequenceName = "entries_seq", allocationSize = 1)
    Integer entryId;

    String accountNumber;
    String sign;
    BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    Transaction transaction;

}
