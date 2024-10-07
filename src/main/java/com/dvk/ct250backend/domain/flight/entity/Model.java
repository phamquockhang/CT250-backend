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

    @OneToMany(mappedBy = "model", cascade = {CascadeType.ALL}, orphanRemoval = true)
    Set<Airplane> airplanes;
}
