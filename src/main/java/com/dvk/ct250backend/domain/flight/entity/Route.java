package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.enums.RouteTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "routes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Route extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_id_seq")
    @SequenceGenerator(name = "route_id_seq", sequenceName = "routes_seq", allocationSize = 1)
    Integer routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_airport_id")
    Airport destinationAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport_id")
    Airport arrivalAirport;

    String duration;

    @Enumerated(EnumType.STRING)
    RouteTypeEnum routeType;

}
