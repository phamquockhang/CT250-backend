package com.dvk.ct250backend.domain.flight.mapper;

import com.dvk.ct250backend.domain.flight.dto.RouteDTO;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.domain.flight.entity.Route;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AirportMapper.class})
public interface RouteMapper {
    //@Mapping(target = "duration", expression = "java(route.getFlights() != null && !route.getFlights().isEmpty() ? route.getFlights().iterator().next().getFlightDuration() : \"N/A\")")
    @Mapping(target = "duration", expression = "java(route.getFlights() != null && !route.getFlights().isEmpty() ? route.getFlights().iterator().next().getFlightDuration() : null)")
    RouteDTO toRouteDTO(Route route);
    Route toRoute(RouteDTO routeDTO);
    void updateRouteFromDTO(@MappingTarget Route route, RouteDTO routeDTO);

    @BeforeMapping
    default void handleCustomMappings(RouteDTO routeDTO, @MappingTarget Route route) {
        route.setDepartureAirport(Airport.builder().airportId(routeDTO.getDepartureAirport().getAirportId()).build());
        route.setArrivalAirport(Airport.builder().airportId(routeDTO.getArrivalAirport().getAirportId()).build());
    }

}
