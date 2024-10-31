package com.dvk.ct250backend.domain.transaction.entity;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.transaction.enums.TransactionStatusEnum;
import com.dvk.ct250backend.domain.transaction.enums.TransactionTypeEnum;
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
@Table(name = "transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_seq", allocationSize = 1)
    Integer transactionId;

    @Enumerated(EnumType.STRING)
    TransactionTypeEnum transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="booking_id")
    Booking booking;

    String txnRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="payment_method_id")
    PaymentMethod paymentMethod;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    TransactionStatusEnum status;

    @Version
    Integer version;
}
