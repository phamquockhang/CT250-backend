package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FlightMapper {

     FlightDTO toFlightDTO(Flight flight);
     Flight toFlight(FlightDTO flightDTO);
     void updateFlightFromDTO(@MappingTarget Flight flight, FlightDTO flightDTO);
}
