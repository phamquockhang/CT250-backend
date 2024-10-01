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
@Table(name = "fee_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_details_id_seq")
    @SequenceGenerator(name = "fee_details_id_seq", sequenceName = "fee_details_seq", allocationSize = 1)
    Integer feeDetailsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_flight_id")
    BookingFlights bookingFlights;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id")
    Fee fee;

    Double subtotal;

}

