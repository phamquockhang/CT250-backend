package com.dvk.ct250backend.domain.auth.mapper;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "countryId", source = "country.countryId")
    @Mapping(target = "userId", source = "userId")
    UserDTO toUserDTO(User user);

    @Mapping(target = "country.countryId", source = "countryId")
    @Mapping(target = "userId", source = "userId")
    User toUser(UserDTO userDTO);

    @Mapping(target = "password", ignore = true)
    void updateUserFromDTO(UserDTO userDTO, @MappingTarget User user);

    default UserDTO toUserDTOWithNullCheck(User user) {
        UserDTO userDTO = toUserDTO(user);
        if (user.getCountry() == null) {
            userDTO.setCountryId(null);
        }
        return userDTO;
    }
}