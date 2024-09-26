package com.dvk.ct250backend.domain.flight.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airplanes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airplane_id_seq")
    @SequenceGenerator(name = "airplane_id_seq", sequenceName = "airplanes_seq", allocationSize = 1)
    Integer airplaneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    Model model;
}
