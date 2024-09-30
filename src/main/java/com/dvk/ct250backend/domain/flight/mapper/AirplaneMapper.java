package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.AirplaneDTO;
import com.dvk.ct250backend.domain.flight.entity.Airplane;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AirplaneMapper {
    AirplaneDTO toAirplaneDTO(Airplane Airplane);
    Airplane toAirplane(AirplaneDTO AirplaneDTO);
    void updateAirplaneFromDTO(@MappingTarget Airplane Airplane, AirplaneDTO AirplaneDTO);
}
