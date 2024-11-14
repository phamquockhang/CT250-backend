package com.dvk.ct250backend.domain.booking.entity;

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
@Table(name = "special_services")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecialService {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_service_id_seq")
    @SequenceGenerator(name = "special_service_id_seq", sequenceName = "special_services_seq", allocationSize = 1)
    Integer specialServiceId;

    String serviceName;
    String description;
    String conditions;
    Integer maxPassengers;
    Boolean requiredSupport;
    Boolean healthVerification;
    String bookingLeadTime;
    String specialInstructions;
    String serviceType;
    String imgUrl;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "specialServices", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<BookingPassenger> bookingPassengers;
}
