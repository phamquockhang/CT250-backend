package com.dvk.ct250backend.domain.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_detail_lines")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDetailLine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_detail_line_id_seq")
    @SequenceGenerator(name = "booking_detail_line_id_seq", sequenceName = "booking_detail_line_seq", allocationSize = 1)
    Integer bookingDetailLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_segment_id")
    BookingSegment bookingSegment;

    Double singlePrice;
    Double subTotalPrice;
    Integer quantity;

    @OneToMany(mappedBy = "bookingDetailLine", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Set<Fee> fees;

}
