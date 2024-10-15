package com.dvk.ct250backend.domain.flight.entity;

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
@Table(name = "models")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_id_seq")
    @SequenceGenerator(name = "model_id_seq", sequenceName = "models_seq", allocationSize = 1)
    Integer modelId;


    String modelName;

    @OneToMany(mappedBy = "model", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Set<Airplane> airplanes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "model", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Set<Seat> seats; // Danh sách các ghế trên máy bay
}
