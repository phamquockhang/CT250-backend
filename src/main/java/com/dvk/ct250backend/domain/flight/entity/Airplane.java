package com.dvk.ct250backend.domain.flight.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.flight.enums.AirplaneStatusEnum;
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
@Table(name = "airplanes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Airplane extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airplane_id_seq")
    @SequenceGenerator(name = "airplane_id_seq", sequenceName = "airplanes_seq", allocationSize = 1)
    Integer airplaneId;

    String modelName; // Tên mô hình máy bay
    String manufacturer; // Nhà sản xuất
    Integer maxDistance; // Khoảng cách tối đa (km)
    Integer velocity; // Tốc độ bay tối đa (km/h)
    Integer numberOfSeats; // Số ghế
    Double overallLength; // Chiều dài tổng thể (m)
    Double wingspan; // Sải cánh (m)
    Double height;

    @Enumerated(EnumType.STRING)// Chiều cao (m)
    AirplaneStatusEnum status; // Trạng thái máy bay

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "airplane", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Flight> flights; // Danh sách các chuyến bay


}
