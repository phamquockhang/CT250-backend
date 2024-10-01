package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.RouteDTO;
import com.dvk.ct250backend.domain.flight.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AirportMapper.class})
public interface RouteMapper {
    RouteDTO toRouteDTO(Route route);
    Route toRoute(RouteDTO routeDTO);
    void updateRouteFromDTO(@MappingTarget Route route, RouteDTO routeDTO);
}
