package com.dvk.ct250backend.domain.flight.entity;

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
@Table(name = "models")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_id_seq")
    @SequenceGenerator(name = "model_id_seq", sequenceName = "models_seq", allocationSize = 1)
    Integer modelId;         // ID duy nhất của model

    String modelName; // Tên mô hình máy bay
    String manufacturer; // Nhà sản xuất
    Integer maxDistance; // Khoảng cách tối đa (km)
    Integer velocity; // Tốc độ bay tối đa (km/h)

    Integer numberOfSeats; // Số ghế

    Double overallLength; // Chiều dài tổng thể (m)
    Double wingspan; // Sải cánh (m)
    Double height; // Chiều cao (m)

    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    List<Airplane> airplanes;

}
