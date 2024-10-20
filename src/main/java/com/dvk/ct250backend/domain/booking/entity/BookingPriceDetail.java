package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
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
@Table(name = "booking_price_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingPriceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passenger_price_detail_id_seq")
    @SequenceGenerator(name = "passenger_price_detail_id_seq", sequenceName = "passenger_price_detail_seq", allocationSize = 1)
    Integer passengerPriceDetailId;

    @Enumerated(EnumType.STRING)
    PassengerTypeEnum passengerType;

    @Column(precision = 15, scale = 2)
    BigDecimal priceAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    Booking booking;
}
