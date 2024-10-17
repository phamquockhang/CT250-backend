package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
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
@Table(name = "booking_segment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingSegment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_segement_id_seq")
    @SequenceGenerator(name = "booking_segment_id_seq", sequenceName = "booking_segment_seq", allocationSize = 1)
    Integer flightSegmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    Booking booking;

    @OneToMany(mappedBy = "bookingSegment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Set<BookingDetailLine> bookingDetailLines;

    Double totalPrice;


}
