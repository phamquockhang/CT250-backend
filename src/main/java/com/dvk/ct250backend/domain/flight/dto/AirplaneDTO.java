package com.dvk.ct250backend.domain.flight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirplaneDTO {
    Integer airplaneId;

    @NotBlank(message = "Model name is required")
    String modelName; // Tên mô hình máy bay

    @NotBlank(message = "Manufacturer is required")
    String manufacturer; // Nhà sản xuất

    @NotBlank(message = "Max distance is required")
    Integer maxDistance; // Khoảng cách tối đa (km)

    @NotBlank(message = "Velocity is required")
    Integer velocity; // Tốc độ bay tối đa (km/h)

    @NotBlank(message = "Number of seats is required")
    Integer numberOfSeats; // Số ghế

    @NotBlank(message = "Overall length is required")
    Double overallLength; // Chiều dài tổng thể (m)

    @NotBlank(message = "Wingspan is required")
    Double wingspan; // Sải cánh (m)

    @NotBlank(message = "Height is required")
    Double height;


    String status; // Trạng thái máy bay

    boolean inUse; // Đang sử dụng hay không

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;

}
