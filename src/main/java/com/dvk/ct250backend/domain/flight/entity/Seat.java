package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.flight.enums.SeatClassEnum;
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
@Table(name = "seats")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_id_seq")
    @SequenceGenerator(name = "seat_id_seq", sequenceName = "seat_seq", allocationSize = 1)
    Integer seatId;

    @Enumerated(EnumType.STRING)
    SeatClassEnum seatClass;

    String seatCode;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seat", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<SeatAvailability> seatAvailability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    Model model;

}
