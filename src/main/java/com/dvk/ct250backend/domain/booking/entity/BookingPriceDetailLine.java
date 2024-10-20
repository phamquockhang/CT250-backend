package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.entity.Fee;
import com.dvk.ct250backend.domain.flight.enums.TicketClassEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_price_detail_lines")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingPriceDetailLine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_detail_line_id_seq")
    @SequenceGenerator(name = "booking_detail_line_id_seq", sequenceName = "booking_detail_line_seq", allocationSize = 1)
    Integer bookingDetailLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_price_detail_id")
    BookingPriceDetail bookingPriceDetail;

    @Enumerated(EnumType.STRING)
    TicketClassEnum ticketClass;

    double ticketFee;
    Integer quantity;
    double totalAmount;

//    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "bookingPriceDetailLines", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    List<Fee> fees;

}
