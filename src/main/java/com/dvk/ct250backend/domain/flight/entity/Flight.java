package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.enums.FlightStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_id_seq")
    @SequenceGenerator(name = "flight_id_seq", sequenceName = "flights_seq", allocationSize = 1)
    Integer flightId;

    @Column(unique = true, nullable = false)
    String flightName;

    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id")
    Route route;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flight", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<FlightPricing> flightPricing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id", nullable = false)
    Airplane airplane;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flight", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<SeatAvailability> seatAvailability;

    @Enumerated(EnumType.STRING)
    FlightStatusEnum flightStatus;

    @Transient
    public String getFlightDuration() {
        if (departureDateTime != null && arrivalDateTime != null) {
            Duration duration = Duration.between(departureDateTime, arrivalDateTime);
            long totalMinutes = duration.toMinutes();
            return totalMinutes + " minutes";
        }
        return null;
    }


}
