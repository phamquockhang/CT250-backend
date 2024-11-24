package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.enums.GenderEnum;
import com.dvk.ct250backend.domain.booking.dto.PassengerDTO;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.country.entity.Country;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    PassengerDTO toPassengerDTO(Passenger passenger);
    Passenger toPassenger(PassengerDTO passengerDTO);
    void updatePassengerFromDTO(@MappingTarget Passenger passenger, PassengerDTO passengerDTO);


    @BeforeMapping
    default void handleCustomMappings(PassengerDTO passengerDTO, @MappingTarget Passenger passenger) {
        if (passengerDTO.getCountry() != null) {
            passenger.setCountry(Country.builder().countryId(passengerDTO.getCountry().getCountryId()).build());
        }
        if (passengerDTO.getUser() != null) {
            User user = passenger.getUser();
            if (user == null || !user.getUserId().equals(passengerDTO.getUser().getUserId())) {
                passenger.setUser(User.builder().userId(passengerDTO.getUser().getUserId()).build());
            }
        }
        passenger.setFirstName(passengerDTO.getFirstName());
        passenger.setLastName(passengerDTO.getLastName());
        passenger.setDateOfBirth(passengerDTO.getDateOfBirth());
        passenger.setGender(GenderEnum.valueOf(passengerDTO.getGender().name()));
        passenger.setPhoneNumber(passengerDTO.getPhoneNumber());
        passenger.setEmail(passengerDTO.getEmail());
    }
}
