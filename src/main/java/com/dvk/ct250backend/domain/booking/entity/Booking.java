//package com.dvk.ct250backend.domain.booking.entity;
//
//import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
//import com.dvk.ct250backend.domain.booking.enums.TripTypeEnum;
//import com.dvk.ct250backend.domain.common.entity.BaseEntity;
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.util.Set;
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "bookings")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class Booking extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_seq")
//    @SequenceGenerator(name = "booking_id_seq", sequenceName = "bookings_seq", allocationSize = 1)
//    Integer bookingId;
//
//    @Enumerated(EnumType.STRING)
//    TripTypeEnum tripType;
//
//    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
//    Set<BookingFlights> bookingFlights;
//
//}
