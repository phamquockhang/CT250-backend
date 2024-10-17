package com.dvk.ct250backend.domain.booking.entity;

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
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_id_seq")
    @SequenceGenerator(name = "fee_id_seq", sequenceName = "fees_seq", allocationSize = 1)
    Integer feeId;

    String feeName;
    Double amount;
    String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_detail_line_id")
    BookingDetailLine bookingDetailLine;

}
